package com.school.teacherarchivesystem.entity;

import com.school.teacherarchivesystem.enums.ActionType;
import jakarta.persistence.*;

@Entity
@Table(name = "system_log")
public class SystemLog extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", length = 50)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false, length = 50)
    private ActionType actionType;

    @Column(nullable = false, length = 50)
    private String module;

    @Column(name = "biz_id")
    private Long bizId;

    @Column(nullable = false)
    private Boolean success;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String detail;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public Long getBizId() { return bizId; }
    public void setBizId(Long bizId) { this.bizId = bizId; }
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
}
