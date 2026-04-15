package com.school.teacherarchivesystem.entity;

import com.school.teacherarchivesystem.enums.BackupStatus;
import com.school.teacherarchivesystem.enums.BackupType;
import jakarta.persistence.*;

@Entity
@Table(name = "backup_record")
public class BackupRecord extends BaseEntity {

    @Column(name = "backup_name", nullable = false, length = 100)
    private String backupName;

    @Enumerated(EnumType.STRING)
    @Column(name = "backup_type", nullable = false, length = 20)
    private BackupType backupType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BackupStatus status;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(length = 300)
    private String remark;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private SystemUser createdBy;

    public String getBackupName() { return backupName; }
    public void setBackupName(String backupName) { this.backupName = backupName; }
    public BackupType getBackupType() { return backupType; }
    public void setBackupType(BackupType backupType) { this.backupType = backupType; }
    public BackupStatus getStatus() { return status; }
    public void setStatus(BackupStatus status) { this.status = status; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public SystemUser getCreatedBy() { return createdBy; }
    public void setCreatedBy(SystemUser createdBy) { this.createdBy = createdBy; }
}
