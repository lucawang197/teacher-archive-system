package com.school.teacherarchivesystem.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.teacherarchivesystem.common.BusinessException;
import com.school.teacherarchivesystem.dto.evaluation.*;
import com.school.teacherarchivesystem.entity.*;
import com.school.teacherarchivesystem.enums.ActionType;
import com.school.teacherarchivesystem.enums.ArchiveStatus;
import com.school.teacherarchivesystem.enums.ArchiveType;
import com.school.teacherarchivesystem.enums.UserRole;
import com.school.teacherarchivesystem.enums.UserStatus;
import com.school.teacherarchivesystem.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EvaluationService {
    private final EvaluationTemplateRepository templateRepository;
    private final EvaluationResultRepository resultRepository;
    private final EvaluationIndicatorRepository indicatorRepository;
    private final SystemUserRepository userRepository;
    private final ArchiveRecordRepository archiveRecordRepository;
    private final LogService logService;
    private final ObjectMapper objectMapper;

    public EvaluationService(EvaluationTemplateRepository templateRepository, EvaluationResultRepository resultRepository,
                             EvaluationIndicatorRepository indicatorRepository, SystemUserRepository userRepository,
                             ArchiveRecordRepository archiveRecordRepository, LogService logService, ObjectMapper objectMapper) {
        this.templateRepository = templateRepository;
        this.resultRepository = resultRepository;
        this.indicatorRepository = indicatorRepository;
        this.userRepository = userRepository;
        this.archiveRecordRepository = archiveRecordRepository;
        this.logService = logService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public EvaluationTemplate createTemplate(EvaluationTemplateRequest request, HttpServletRequest servletRequest) {
        EvaluationTemplate template = new EvaluationTemplate();
        template.setTemplateName(request.getTemplateName());
        template.setSchoolYear(request.getSchoolYear());
        template.setSubjectName(request.getSubjectName());
        template.setActive(false);
        for (EvaluationIndicatorRequest item : request.getIndicators()) {
            EvaluationIndicator indicator = new EvaluationIndicator();
            indicator.setTemplate(template);
            indicator.setIndicatorCode(item.getIndicatorCode());
            indicator.setIndicatorName(item.getIndicatorName());
            indicator.setWeight(item.getWeight());
            indicator.setScoreRuleJson(item.getScoreRuleJson());
            indicator.setSortOrder(item.getSortOrder() == null ? 1 : item.getSortOrder());
            indicator.setEnabled(item.getEnabled() == null ? true : item.getEnabled());
            template.getIndicators().add(indicator);
        }
        EvaluationTemplate saved = templateRepository.save(template);
        logService.record(ActionType.CREATE_TEMPLATE, "EVALUATION", saved.getId(), true, "新建考评模板", servletRequest);
        return saved;
    }

    @Transactional
    public EvaluationTemplate activate(Long id, HttpServletRequest servletRequest) {
        templateRepository.findAll().forEach(item -> {
            item.setActive(Objects.equals(item.getId(), id));
            templateRepository.save(item);
        });
        EvaluationTemplate template = templateRepository.findById(id).orElseThrow(() -> new BusinessException("模板不存在"));
        logService.record(ActionType.ACTIVATE_TEMPLATE, "EVALUATION", id, true, "模板生效", servletRequest);
        return template;
    }

    public List<EvaluationTemplate> listTemplates() {
        return templateRepository.findAll();
    }

    @Transactional
    public List<EvaluationResult> runScoring(ScoringRequest request, HttpServletRequest servletRequest) {
        EvaluationTemplate template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new BusinessException("模板不存在"));
        List<EvaluationIndicator> indicators = indicatorRepository.findByTemplateIdAndEnabledTrueOrderBySortOrderAsc(template.getId());
        if (indicators.isEmpty()) {
            throw new BusinessException("模板下没有可用指标");
        }
        resultRepository.deleteByTemplateId(template.getId());
        List<SystemUser> teachers = userRepository.findByRoleAndStatusOrderByIdAsc(UserRole.TEACHER, UserStatus.ENABLED);
        List<EvaluationResult> resultList = new ArrayList<>();
        for (SystemUser teacher : teachers) {
            EvaluationResult result = buildResult(template, teacher, indicators);
            resultList.add(resultRepository.save(result));
        }
        resultList.sort(Comparator.comparing(EvaluationResult::getTotalScore).reversed());
        for (int i = 0; i < resultList.size(); i++) {
            EvaluationResult result = resultList.get(i);
            result.setRankingNo(i + 1);
            resultRepository.save(result);
        }
        logService.record(ActionType.RUN_SCORING, "EVALUATION", template.getId(), true, "执行自动评分", servletRequest);
        return resultList;
    }

    public List<EvaluationResult> listResults(Long templateId) {
        if (templateId != null) {
            return resultRepository.findByTemplateIdOrderByRankingNoAsc(templateId);
        }
        EvaluationTemplate active = templateRepository.findByActiveTrue().orElseThrow(() -> new BusinessException("暂无已生效模板"));
        return resultRepository.findByTemplateIdOrderByRankingNoAsc(active.getId());
    }

    @Transactional
    public EvaluationResult remark(Long id, ResultRemarkRequest request) {
        EvaluationResult result = resultRepository.findById(id).orElseThrow(() -> new BusinessException("结果不存在"));
        result.setRemark(request.getRemark());
        return resultRepository.save(result);
    }

    private EvaluationResult buildResult(EvaluationTemplate template, SystemUser teacher, List<EvaluationIndicator> indicators) {
        EvaluationResult result = new EvaluationResult();
        result.setTemplate(template);
        result.setTeacher(teacher);
        result.setScoreTime(LocalDateTime.now());
        List<String> missingItems = new ArrayList<>();
        double total = 0D;
        for (EvaluationIndicator indicator : indicators) {
            Map<String, Object> rule = parseRule(indicator.getScoreRuleJson());
            double unitScore = toDouble(rule.getOrDefault("unitScore", 0));
            double maxScore = toDouble(rule.getOrDefault("maxScore", 100));
            long count = countByIndicatorCode(teacher.getId(), indicator.getIndicatorCode());
            double rawScore = Math.min(count * unitScore, maxScore);
            if (count == 0) {
                missingItems.add(indicator.getIndicatorName());
            }
            double weighted = rawScore * indicator.getWeight() / 100.0;
            total += weighted;

            EvaluationResultDetail detail = new EvaluationResultDetail();
            detail.setResult(result);
            detail.setIndicatorCode(indicator.getIndicatorCode());
            detail.setIndicatorName(indicator.getIndicatorName());
            detail.setRawScore(rawScore);
            detail.setWeight(indicator.getWeight());
            detail.setWeightedScore(weighted);
            result.getDetails().add(detail);
        }
        result.setTotalScore(Math.round(total * 100.0) / 100.0);
        try {
            result.setMissingItemsJson(objectMapper.writeValueAsString(missingItems));
        } catch (Exception ex) {
            result.setMissingItemsJson("[]");
        }
        return result;
    }

    private long countByIndicatorCode(Long teacherId, String indicatorCode) {
        ArchiveType type;
        try {
            type = ArchiveType.valueOf(indicatorCode);
        } catch (Exception ex) {
            return 0L;
        }
        return archiveRecordRepository.countByTeacherIdAndArchiveTypeAndStatusAndDeletedFalse(teacherId, type, ArchiveStatus.APPROVED);
    }

    private Map<String, Object> parseRule(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception ex) {
            return Map.of("unitScore", 0, "maxScore", 100);
        }
    }

    private double toDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        return Double.parseDouble(String.valueOf(value));
    }
}
