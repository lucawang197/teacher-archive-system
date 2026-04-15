package com.school.teacherarchivesystem.dto.backup;

public class BackupRequest {
    private String backupName;
    private String remark;

    public String getBackupName() { return backupName; }
    public void setBackupName(String backupName) { this.backupName = backupName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
