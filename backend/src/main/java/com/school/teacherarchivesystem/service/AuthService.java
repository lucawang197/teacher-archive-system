package com.school.teacherarchivesystem.service;

import com.school.teacherarchivesystem.common.BusinessException;
import com.school.teacherarchivesystem.config.AppProperties;
import com.school.teacherarchivesystem.dto.auth.LoginRequest;
import com.school.teacherarchivesystem.dto.auth.LoginResponse;
import com.school.teacherarchivesystem.entity.SystemUser;
import com.school.teacherarchivesystem.enums.ActionType;
import com.school.teacherarchivesystem.enums.UserStatus;
import com.school.teacherarchivesystem.repository.SystemUserRepository;
import com.school.teacherarchivesystem.security.JwtTokenProvider;
import com.school.teacherarchivesystem.security.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final SystemUserRepository systemUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppProperties appProperties;
    private final LogService logService;

    public AuthService(SystemUserRepository systemUserRepository, PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider, AppProperties appProperties, LogService logService) {
        this.systemUserRepository = systemUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.appProperties = appProperties;
        this.logService = logService;
    }

    public LoginResponse login(LoginRequest request, HttpServletRequest servletRequest) {
        SystemUser user = systemUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));
        if (user.getStatus() != UserStatus.ENABLED) {
            throw new BusinessException("账号已被禁用");
        }
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            throw new BusinessException("账号已锁定，请稍后再试");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            int failedCount = (user.getFailedLoginCount() == null ? 0 : user.getFailedLoginCount()) + 1;
            user.setFailedLoginCount(failedCount);
            if (failedCount >= appProperties.getSecurity().getMaxFailedLogin()) {
                user.setLockedUntil(LocalDateTime.now().plusMinutes(appProperties.getSecurity().getLockMinutes()));
            }
            systemUserRepository.save(user);
            logService.record(ActionType.LOGIN_FAILED, "AUTH", user.getId(), false, "登录失败", servletRequest);
            throw new BusinessException("用户名或密码错误");
        }
        user.setFailedLoginCount(0);
        user.setLockedUntil(null);
        user.setLastLoginAt(LocalDateTime.now());
        user.setLastLoginIp(servletRequest == null ? null : servletRequest.getRemoteAddr());
        systemUserRepository.save(user);

        LoginUser loginUser = new LoginUser(user.getId(), user.getUsername(), user.getRealName(), user.getRole());
        LoginResponse response = new LoginResponse();
        response.setToken(jwtTokenProvider.createToken(loginUser));
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setRole(user.getRole().name());
        logService.record(ActionType.LOGIN, "AUTH", user.getId(), true, "登录成功", servletRequest);
        return response;
    }

    public String forgotPassword(String username, HttpServletRequest servletRequest) {
        systemUserRepository.findByUsername(username).orElseThrow(() -> new BusinessException("用户不存在"));
        logService.record(ActionType.FORGOT_PASSWORD, "AUTH", null, true, "触发重置密码说明", servletRequest);
        return "演示版本已记录重置密码请求。正式环境请通过短信或邮箱验证码完成密码重置。";
    }
}
