package com.school.teacherarchivesystem.controller;

import com.school.teacherarchivesystem.common.ApiResponse;
import com.school.teacherarchivesystem.dto.user.ChangePasswordRequest;
import com.school.teacherarchivesystem.dto.user.UserCreateRequest;
import com.school.teacherarchivesystem.dto.user.UserUpdateRequest;
import com.school.teacherarchivesystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> createTeacher(@RequestBody @Valid UserCreateRequest request, HttpServletRequest servletRequest) {
        return ApiResponse.ok("新增成功", userService.createTeacher(request, servletRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> updateTeacher(@PathVariable Long id, @RequestBody UserUpdateRequest request, HttpServletRequest servletRequest) {
        return ApiResponse.ok("更新成功", userService.updateTeacher(id, request, servletRequest));
    }

    @GetMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> listTeachers() {
        return ApiResponse.ok(userService.listTeachers());
    }

    @PostMapping("/change-password")
    public ApiResponse<?> changePassword(@RequestBody @Valid ChangePasswordRequest request, HttpServletRequest servletRequest) {
        return ApiResponse.ok(userService.changePassword(request, servletRequest));
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> importTeachers(@RequestPart("file") MultipartFile file) {
        return ApiResponse.ok("演示版本已预留批量导入接口，待接入 Excel 解析", file.getOriginalFilename());
    }
}
