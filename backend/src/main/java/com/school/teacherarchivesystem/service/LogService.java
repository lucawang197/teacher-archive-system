package com.school.teacherarchivesystem.service;

import com.school.teacherarchivesystem.entity.SystemLog;
import com.school.teacherarchivesystem.enums.ActionType;
import com.school.teacherarchivesystem.repository.SystemLogRepository;
import com.school.teacherarchivesystem.security.LoginUser;
import com.school.teacherarchivesystem.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class LogService {
    private final SystemLogRepository systemLogRepository;
    public LogService(SystemLogRepository systemLogRepository) { this.systemLogRepository = systemLogRepository; }
    public void record(ActionType actionType, String module, Long bizId, boolean success, String detail, HttpServletRequest request) {
        SystemLog log = new SystemLog();
        try {
            LoginUser user = SecurityUtils.currentUser();
            log.setUserId(user.getUserId());
            log.setUsername(user.getUsername());
        } catch (Exception ignored) {}
        log.setActionType(actionType);
        log.setModule(module);
        log.setBizId(bizId);
        log.setSuccess(success);
        log.setDetail(detail);
        if (request != null) log.setIpAddress(request.getRemoteAddr());
        systemLogRepository.save(log);
    }

    public Page<SystemLog> list(String username, String module, Boolean success, LocalDate dateFrom, LocalDate dateTo, Pageable pageable) {
        return systemLogRepository.findAll(buildSpec(username, module, success, dateFrom, dateTo), pageable);
    }

    public List<SystemLog> export(String username, String module, Boolean success, LocalDate dateFrom, LocalDate dateTo) {
        return systemLogRepository.findAll(buildSpec(username, module, success, dateFrom, dateTo));
    }

    private Specification<SystemLog> buildSpec(String username, String module, Boolean success, LocalDate dateFrom, LocalDate dateTo) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();
            if (username != null && !username.isBlank()) {
                predicates = cb.and(predicates, cb.like(root.get("username"), "%" + username.trim() + "%"));
            }
            if (module != null && !module.isBlank()) {
                predicates = cb.and(predicates, cb.like(root.get("module"), "%" + module.trim() + "%"));
            }
            if (success != null) {
                predicates = cb.and(predicates, cb.equal(root.get("success"), success));
            }
            if (dateFrom != null) {
                predicates = cb.and(predicates, cb.greaterThanOrEqualTo(root.get("createdAt"), dateFrom.atStartOfDay()));
            }
            if (dateTo != null) {
                LocalDateTime end = dateTo.atTime(LocalTime.MAX);
                predicates = cb.and(predicates, cb.lessThanOrEqualTo(root.get("createdAt"), end));
            }
            query.orderBy(cb.desc(root.get("createdAt")));
            return predicates;
        };
    }
}
