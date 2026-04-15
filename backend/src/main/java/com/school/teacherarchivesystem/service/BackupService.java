package com.school.teacherarchivesystem.service;

import com.school.teacherarchivesystem.common.BusinessException;
import com.school.teacherarchivesystem.config.AppProperties;
import com.school.teacherarchivesystem.entity.BackupRecord;
import com.school.teacherarchivesystem.entity.SystemUser;
import com.school.teacherarchivesystem.enums.ActionType;
import com.school.teacherarchivesystem.enums.BackupStatus;
import com.school.teacherarchivesystem.enums.BackupType;
import com.school.teacherarchivesystem.repository.BackupRecordRepository;
import com.school.teacherarchivesystem.repository.SystemUserRepository;
import com.school.teacherarchivesystem.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
public class BackupService {
    private final BackupRecordRepository backupRecordRepository;
    private final SystemUserRepository userRepository;
    private final AppProperties appProperties;
    private final LogService logService;

    public BackupService(BackupRecordRepository backupRecordRepository, SystemUserRepository userRepository, AppProperties appProperties, LogService logService) {
        this.backupRecordRepository = backupRecordRepository;
        this.userRepository = userRepository;
        this.appProperties = appProperties;
        this.logService = logService;
    }

    public BackupRecord createManualBackup(String backupName, String remark, HttpServletRequest request) {
        try {
            Path backupDir = Path.of(appProperties.getStorage().getBackupPath());
            Files.createDirectories(backupDir);
            String filename = LocalDateTime.now().toString().replace(":", "-") + "_" + backupName + ".bak.txt";
            Path file = backupDir.resolve(filename);
            Files.writeString(file, "本示例项目交付的是应用层备份占位实现。\n生产环境建议由数据库 mysqldump + 附件目录打包统一执行。\n");

            SystemUser operator = userRepository.findById(SecurityUtils.currentUser().getUserId()).orElse(null);
            BackupRecord record = new BackupRecord();
            record.setBackupName(backupName);
            record.setBackupType(BackupType.MANUAL);
            record.setStatus(BackupStatus.SUCCESS);
            record.setRemark(remark);
            record.setFilePath(file.toString());
            record.setCreatedBy(operator);
            BackupRecord saved = backupRecordRepository.save(record);
            logService.record(ActionType.CREATE_BACKUP, "BACKUP", saved.getId(), true, "创建手动备份", request);
            return saved;
        } catch (Exception ex) {
            throw new BusinessException("备份失败：" + ex.getMessage());
        }
    }

    public Page<BackupRecord> list(Pageable pageable) {
        return backupRecordRepository.findAll(pageable);
    }

    public String restore(Long backupId, HttpServletRequest request) {
        BackupRecord record = backupRecordRepository.findById(backupId)
                .orElseThrow(() -> new BusinessException("备份记录不存在"));
        logService.record(ActionType.RESTORE_BACKUP, "BACKUP", backupId, true, "恢复演示", request);
        return "演示版本未直接执行数据库覆盖恢复。正式部署请调用 mysqldump / mysql 命令或运维脚本完成恢复。备份文件：" + record.getFilePath();
    }
}
