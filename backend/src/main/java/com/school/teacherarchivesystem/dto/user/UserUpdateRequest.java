package com.school.teacherarchivesystem.dto.user;

public class UserUpdateRequest {
    private String realName;
    private String subjectName;
    private String jobTitle;
    private String phone;
    private String email;
    private Boolean enabled;
    private String status;
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
