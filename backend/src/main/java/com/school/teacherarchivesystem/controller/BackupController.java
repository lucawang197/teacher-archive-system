package com.school.teacherarchivesystem.controller;

import com.school.teacherarchivesystem.common.ApiResponse;
import com.school.teacherarchivesystem.dto.backup.BackupRequest;
import com.school.teacherarchivesystem.service.BackupService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backups")
@PreAuthorize("hasRole('ADMIN')")
public class BackupController {
    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @PostMapping
    public ApiResponse<?> create(@RequestBody @Valid BackupRequest request, HttpServletRequest servletRequest) {
        return ApiResponse.ok("备份成功", backupService.createManualBackup(request.getBackupName(), request.getRemark(), servletRequest));
    }

    @GetMapping
    public ApiResponse<?> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(backupService.list(PageRequest.of(page, size)));
    }

    @PostMapping("/{id}/restore")
    public ApiResponse<?> restore(@PathVariable Long id, HttpServletRequest servletRequest) {
        return ApiResponse.ok("恢复请求已记录", backupService.restore(id, servletRequest));
    }
}
