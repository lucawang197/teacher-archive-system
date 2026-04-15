package com.school.teacherarchivesystem.security;

import com.school.teacherarchivesystem.enums.UserRole;

public class LoginUser {
    private Long userId;
    private String username;
    private String realName;
    private UserRole role;

    public LoginUser() {}

    public LoginUser(Long userId, String username, String realName, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.realName = realName;
        this.role = role;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}
