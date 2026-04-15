package com.school.teacherarchivesystem.service;

import com.school.teacherarchivesystem.common.BusinessException;
import com.school.teacherarchivesystem.dto.user.ChangePasswordRequest;
import com.school.teacherarchivesystem.dto.user.UserCreateRequest;
import com.school.teacherarchivesystem.dto.user.UserUpdateRequest;
import com.school.teacherarchivesystem.entity.SystemUser;
import com.school.teacherarchivesystem.enums.ActionType;
import com.school.teacherarchivesystem.enums.UserRole;
import com.school.teacherarchivesystem.enums.UserStatus;
import com.school.teacherarchivesystem.repository.SystemUserRepository;
import com.school.teacherarchivesystem.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final SystemUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final LogService logService;

    public UserService(SystemUserRepository repository, PasswordEncoder passwordEncoder, LogService logService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.logService = logService;
    }

    public SystemUser createTeacher(UserCreateRequest request, HttpServletRequest servletRequest) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        if (repository.existsByTeacherNo(request.getTeacherNo())) {
            throw new BusinessException("工号已存在");
        }
        SystemUser user = new SystemUser();
        user.setUsername(request.getUsername());
        user.setRealName(request.getRealName());
        user.setTeacherNo(request.getTeacherNo());
        user.setSubjectName(request.getSubjectName());
        user.setJobTitle(request.getJobTitle());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setRole(UserRole.TEACHER);
        user.setStatus(Boolean.TRUE.equals(request.getEnabled()) ? UserStatus.ENABLED : UserStatus.DISABLED);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword() == null || request.getPassword().isBlank() ? "123456" : request.getPassword()));
        SystemUser saved = repository.save(user);
        logService.record(ActionType.CREATE_USER, "USER", saved.getId(), true, "新增教师账号", servletRequest);
        return saved;
    }

    public SystemUser updateTeacher(Long id, UserUpdateRequest request, HttpServletRequest servletRequest) {
        SystemUser user = repository.findById(id).orElseThrow(() -> new BusinessException("用户不存在"));
        if (user.getRole() != UserRole.TEACHER) {
            throw new BusinessException("仅支持维护教师账号");
        }
        if (request.getRealName() != null) user.setRealName(request.getRealName());
        if (request.getSubjectName() != null) user.setSubjectName(request.getSubjectName());
        if (request.getJobTitle() != null) user.setJobTitle(request.getJobTitle());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getEnabled() != null) {
            user.setStatus(Boolean.TRUE.equals(request.getEnabled()) ? UserStatus.ENABLED : UserStatus.DISABLED);
        }
        SystemUser saved = repository.save(user);
        logService.record(ActionType.UPDATE_USER, "USER", saved.getId(), true, "更新教师账号", servletRequest);
        return saved;
    }

    public List<SystemUser> listTeachers() {
        return repository.findByRoleOrderByIdAsc(UserRole.TEACHER);
    }

    public String changePassword(ChangePasswordRequest request, HttpServletRequest servletRequest) {
        SystemUser user = repository.findById(SecurityUtils.currentUser().getUserId()).orElseThrow(() -> new BusinessException("用户不存在"));
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new BusinessException("原密码错误");
        }
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
        logService.record(ActionType.CHANGE_PASSWORD, "USER", user.getId(), true, "修改密码", servletRequest);
        return "密码修改成功";
    }
}
