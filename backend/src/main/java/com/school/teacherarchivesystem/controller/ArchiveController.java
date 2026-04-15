package com.school.teacherarchivesystem.controller;

import com.school.teacherarchivesystem.common.ApiResponse;
import com.school.teacherarchivesystem.dto.archive.ArchiveFormRequest;
import com.school.teacherarchivesystem.dto.archive.ArchiveReviewRequest;
import com.school.teacherarchivesystem.service.ArchiveService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/archives")
public class ArchiveController {
    private final ArchiveService archiveService;

    public ArchiveController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<?> create(@RequestPart("form") @Valid ArchiveFormRequest form,
                                 @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                 HttpServletRequest request) {
        return ApiResponse.ok("提交成功", archiveService.create(form, files, request));
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<?> update(@PathVariable Long id,
                                 @RequestPart("form") @Valid ArchiveFormRequest form,
                                 @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                 HttpServletRequest request) {
        return ApiResponse.ok("更新成功", archiveService.update(id, form, files, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<?> delete(@PathVariable Long id, HttpServletRequest request) {
        archiveService.delete(id, request);
        return ApiResponse.ok("删除成功", null);
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<?> mine(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(archiveService.mine(PageRequest.of(page, size)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> page(@RequestParam(required = false) String teacherName,
                               @RequestParam(required = false) String archiveType,
                               @RequestParam(required = false) String status,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(archiveService.page(teacherName, archiveType, status, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> detail(@PathVariable Long id) {
        return ApiResponse.ok(archiveService.detail(id));
    }

    @PostMapping("/{id}/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> review(@PathVariable Long id, @RequestBody @Valid ArchiveReviewRequest request, HttpServletRequest servletRequest) {
        return ApiResponse.ok("审核成功", archiveService.review(id, request, servletRequest));
    }

    @GetMapping("/summary")
    public ApiResponse<?> summary() {
        return ApiResponse.ok(archiveService.summary());
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> stats() {
        return ApiResponse.ok(archiveService.statsPerTeacher());
    }
}
