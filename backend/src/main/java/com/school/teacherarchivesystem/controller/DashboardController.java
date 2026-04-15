package com.school.teacherarchivesystem.controller;

import com.school.teacherarchivesystem.common.ApiResponse;
import com.school.teacherarchivesystem.security.LoginUser;
import com.school.teacherarchivesystem.service.ArchiveService;
import com.school.teacherarchivesystem.util.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final ArchiveService archiveService;

    public DashboardController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @GetMapping("/summary")
    public ApiResponse<?> summary() {
        LoginUser current = SecurityUtils.currentUser();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("currentUser", Map.of(
                "userId", current.getUserId(),
                "username", current.getUsername(),
                "realName", current.getRealName(),
                "role", current.getRole().name()
        ));
        result.putAll(archiveService.summary());
        return ApiResponse.ok(result);
    }
}
