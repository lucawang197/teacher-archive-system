package com.school.teacherarchivesystem.controller;

import com.school.teacherarchivesystem.common.ApiResponse;
import com.school.teacherarchivesystem.dto.auth.ForgotPasswordRequest;
import com.school.teacherarchivesystem.dto.auth.LoginRequest;
import com.school.teacherarchivesystem.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody @Valid LoginRequest request, HttpServletRequest servletRequest) {
        return ApiResponse.ok("登录成功", authService.login(request, servletRequest));
    }

    @PostMapping("/forgot-password")
    public ApiResponse<?> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request, HttpServletRequest servletRequest) {
        return ApiResponse.ok(authService.forgotPassword(request.getUsername(), servletRequest));
    }
}
