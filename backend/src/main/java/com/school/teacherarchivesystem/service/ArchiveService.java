package com.school.teacherarchivesystem.service;

import com.school.teacherarchivesystem.common.BusinessException;
import com.school.teacherarchivesystem.dto.archive.ArchiveFormRequest;
import com.school.teacherarchivesystem.dto.archive.ArchiveReviewRequest;
import com.school.teacherarchivesystem.entity.ArchiveAttachment;
import com.school.teacherarchivesystem.entity.ArchiveRecord;
import com.school.teacherarchivesystem.entity.SystemUser;
import com.school.teacherarchivesystem.enums.ActionType;
import com.school.teacherarchivesystem.enums.ArchiveStatus;
import com.school.teacherarchivesystem.enums.UserRole;
import com.school.teacherarchivesystem.repository.ArchiveRecordRepository;
import com.school.teacherarchivesystem.repository.SystemUserRepository;
import com.school.teacherarchivesystem.util.FileStorageService;
import com.school.teacherarchivesystem.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArchiveService {
    private final ArchiveRecordRepository archiveRecordRepository;
    private final SystemUserRepository systemUserRepository;
    private final FileStorageService fileStorageService;
    private final LogService logService;

    public ArchiveService(ArchiveRecordRepository archiveRecordRepository, SystemUserRepository systemUserRepository,
                          FileStorageService fileStorageService, LogService logService) {
        this.archiveRecordRepository = archiveRecordRepository;
        this.systemUserRepository = systemUserRepository;
        this.fileStorageService = fileStorageService;
        this.logService = logService;
    }

    @Transactional
    public ArchiveRecord create(ArchiveFormRequest form, List<MultipartFile> files, HttpServletRequest request) {
        SystemUser teacher = systemUserRepository.findById(SecurityUtils.currentUser().getUserId())
                .orElseThrow(() -> new BusinessException("当前教师不存在"));
        ArchiveRecord record = new ArchiveRecord();
        applyForm(record, form);
        record.setTeacher(teacher);
        record.setStatus(ArchiveStatus.PENDING);
        archiveRecordRepository.save(record);
        attachFiles(record, teacher, files);
        ArchiveRecord saved = archiveRecordRepository.save(record);
        logService.record(ActionType.CREATE_ARCHIVE, "ARCHIVE", saved.getId(), true, "提交档案", request);
        return saved;
    }

    @Transactional
    public ArchiveRecord update(Long id, ArchiveFormRequest form, List<MultipartFile> files, HttpServletRequest request) {
        ArchiveRecord record = getAccessibleRecord(id);
        if (record.getStatus() == ArchiveStatus.APPROVED) {
            throw new BusinessException("已审核通过的档案不可直接编辑");
        }
        applyForm(record, form);
        record.setStatus(ArchiveStatus.PENDING);
        if (files != null && !files.isEmpty()) {
            record.getAttachments().clear();
            attachFiles(record, record.getTeacher(), files);
        }
        ArchiveRecord saved = archiveRecordRepository.save(record);
        logService.record(ActionType.UPDATE_ARCHIVE, "ARCHIVE", saved.getId(), true, "更新档案", request);
        return saved;
    }

    @Transactional
    public void delete(Long id, HttpServletRequest request) {
        ArchiveRecord record = getAccessibleRecord(id);
        if (record.getStatus() == ArchiveStatus.APPROVED) {
            throw new BusinessException("已审核通过的档案不可删除");
        }
        record.setDeleted(true);
        archiveRecordRepository.save(record);
        logService.record(ActionType.DELETE_ARCHIVE, "ARCHIVE", id, true, "删除档案", request);
    }

    public Page<ArchiveRecord> mine(Pageable pageable) {
        Long userId = SecurityUtils.currentUser().getUserId();
        Specification<ArchiveRecord> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("teacher").get("id"), userId),
                cb.isFalse(root.get("deleted"))
        );
        return archiveRecordRepository.findAll(spec, pageable);
    }

    public Page<ArchiveRecord> page(String teacherName, String archiveType, String status, Pageable pageable) {
        Specification<ArchiveRecord> spec = (root, query, cb) -> {
            var predicate = cb.isFalse(root.get("deleted"));
            if (teacherName != null && !teacherName.isBlank()) {
                predicate = cb.and(predicate, cb.like(root.get("teacher").get("realName"), "%" + teacherName.trim() + "%"));
            }
            if (archiveType != null && !archiveType.isBlank()) {
                predicate = cb.and(predicate, cb.equal(root.get("archiveType"), Enum.valueOf(com.school.teacherarchivesystem.enums.ArchiveType.class, archiveType)));
            }
            if (status != null && !status.isBlank()) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), Enum.valueOf(ArchiveStatus.class, status)));
            }
            query.orderBy(cb.desc(root.get("createdAt")));
            return predicate;
        };
        return archiveRecordRepository.findAll(spec, pageable);
    }

    public ArchiveRecord detail(Long id) {
        return getAccessibleOrAdmin(id);
    }

    @Transactional
    public ArchiveRecord review(Long id, ArchiveReviewRequest reviewRequest, HttpServletRequest request) {
        ArchiveRecord record = archiveRecordRepository.findById(id).orElseThrow(() -> new BusinessException("档案不存在"));
        if (Boolean.TRUE.equals(record.getDeleted())) {
            throw new BusinessException("档案不存在");
        }
        SystemUser reviewer = systemUserRepository.findById(SecurityUtils.currentUser().getUserId()).orElseThrow();
        record.setStatus(Boolean.TRUE.equals(reviewRequest.getApproved()) ? ArchiveStatus.APPROVED : ArchiveStatus.REJECTED);
        record.setReviewComment(reviewRequest.getReviewComment());
        record.setReviewBy(reviewer);
        record.setReviewedAt(LocalDateTime.now());
        ArchiveRecord saved = archiveRecordRepository.save(record);
        logService.record(ActionType.REVIEW_ARCHIVE, "ARCHIVE", id, true, "审核档案", request);
        return saved;
    }

    public Map<String, Long> summary() {
        LoginUserView current = new LoginUserView(SecurityUtils.currentUser().getUserId(), SecurityUtils.currentUser().getRole());
        if (current.role() == UserRole.ADMIN) {
            return Map.of(
                    "all", archiveRecordRepository.countByDeletedFalse(),
                    "pending", archiveRecordRepository.countByStatusAndDeletedFalse(ArchiveStatus.PENDING),
                    "approved", archiveRecordRepository.countByStatusAndDeletedFalse(ArchiveStatus.APPROVED)
            );
        }
        return Map.of(
                "mine", archiveRecordRepository.countByTeacherIdAndDeletedFalse(current.userId()),
                "pending", archiveRecordRepository.findAll((root, query, cb) -> cb.and(
                        cb.equal(root.get("teacher").get("id"), current.userId()),
                        cb.equal(root.get("status"), ArchiveStatus.PENDING),
                        cb.isFalse(root.get("deleted"))
                )).stream().count()
        );
    }

    public List<Map<String, Object>> statsPerTeacher() {
        List<ArchiveRecord> approved = archiveRecordRepository.findAll((root, query, cb) ->
                cb.and(cb.equal(root.get("status"), ArchiveStatus.APPROVED), cb.isFalse(root.get("deleted"))));
        Map<Long, Map<String, Object>> byTeacher = new LinkedHashMap<>();
        for (ArchiveRecord r : approved) {
            SystemUser teacher = r.getTeacher();
            byTeacher.computeIfAbsent(teacher.getId(), id -> {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("teacherNo", teacher.getTeacherNo() == null ? "" : teacher.getTeacherNo());
                row.put("teacherName", teacher.getRealName());
                row.put("subjectName", teacher.getSubjectName() == null ? "" : teacher.getSubjectName());
                row.put("openClassCount", 0L);
                row.put("paperCount", 0L);
                row.put("competitionCount", 0L);
                row.put("researchProjectCount", 0L);
                return row;
            });
            Map<String, Object> row = byTeacher.get(teacher.getId());
            String key = switch (r.getArchiveType()) {
                case OPEN_CLASS -> "openClassCount";
                case PAPER -> "paperCount";
                case COMPETITION -> "competitionCount";
                case RESEARCH_PROJECT -> "researchProjectCount";
            };
            row.merge(key, 1L, (a, b) -> (Long) a + (Long) b);
        }
        return new ArrayList<>(byTeacher.values());
    }

    private void applyForm(ArchiveRecord record, ArchiveFormRequest form) {
        record.setArchiveName(form.getArchiveName());
        record.setArchiveType(form.getArchiveType());
        record.setLevel(form.getLevel());
        record.setRelatedDate(form.getRelatedDate());
        record.setDescription(form.getDescription());
    }

    private void attachFiles(ArchiveRecord record, SystemUser teacher, List<MultipartFile> files) {
        if (files == null) { return; }
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) { continue; }
            ArchiveAttachment attachment = fileStorageService.storeArchiveFile(teacher, record.getArchiveType(), file);
            attachment.setArchive(record);
            record.getAttachments().add(attachment);
        }
    }

    private ArchiveRecord getAccessibleRecord(Long id) {
        ArchiveRecord record = archiveRecordRepository.findById(id).orElseThrow(() -> new BusinessException("档案不存在"));
        if (Boolean.TRUE.equals(record.getDeleted())) {
            throw new BusinessException("档案不存在");
        }
        if (!record.getTeacher().getId().equals(SecurityUtils.currentUser().getUserId())) {
            throw new BusinessException("无权操作该档案");
        }
        return record;
    }

    private ArchiveRecord getAccessibleOrAdmin(Long id) {
        ArchiveRecord record = archiveRecordRepository.findById(id).orElseThrow(() -> new BusinessException("档案不存在"));
        if (Boolean.TRUE.equals(record.getDeleted())) {
            throw new BusinessException("档案不存在");
        }
        if (!SecurityUtils.isAdmin() && !record.getTeacher().getId().equals(SecurityUtils.currentUser().getUserId())) {
            throw new BusinessException("无权查看该档案");
        }
        return record;
    }

    private record LoginUserView(Long userId, UserRole role) {}
}
