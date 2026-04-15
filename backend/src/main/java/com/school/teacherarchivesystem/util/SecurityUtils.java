package com.school.teacherarchivesystem.util;

import com.school.teacherarchivesystem.enums.UserRole;
import com.school.teacherarchivesystem.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {}

    public static LoginUser currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUser)) {
            throw new IllegalStateException("当前请求未认证");
        }
        return (LoginUser) authentication.getPrincipal();
    }

    public static boolean isAdmin() {
        try {
            return currentUser().getRole() == UserRole.ADMIN;
        } catch (Exception e) {
            return false;
        }
    }
}
