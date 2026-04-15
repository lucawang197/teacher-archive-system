package com.school.teacherarchivesystem.controller;

import com.school.teacherarchivesystem.common.ApiResponse;
import com.school.teacherarchivesystem.dto.evaluation.*;
import com.school.teacherarchivesystem.service.EvaluationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluations")
public class EvaluationController {
    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping("/templates")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> createTemplate(@RequestBody @Valid EvaluationTemplateRequest request, HttpServletRequest servletRequest) {
        return ApiResponse.ok("创建成功", evaluationService.createTemplate(request, servletRequest));
    }

    @PutMapping("/templates/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> activate(@PathVariable Long id, HttpServletRequest servletRequest) {
        return ApiResponse.ok("启用成功", evaluationService.activate(id, servletRequest));
    }

    @GetMapping("/templates")
    public ApiResponse<?> listTemplates() {
        return ApiResponse.ok(evaluationService.listTemplates());
    }

    @PostMapping("/run")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> run(@RequestBody @Valid ScoringRequest request, HttpServletRequest servletRequest) {
        return ApiResponse.ok("评分成功", evaluationService.runScoring(request, servletRequest));
    }

    @GetMapping("/results")
    public ApiResponse<?> results(@RequestParam(required = false) Long templateId) {
        return ApiResponse.ok(evaluationService.listResults(templateId));
    }

    @PutMapping("/results/{id}/remark")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> remark(@PathVariable Long id, @RequestBody ResultRemarkRequest request) {
        return ApiResponse.ok("备注成功", evaluationService.remark(id, request));
    }
}
