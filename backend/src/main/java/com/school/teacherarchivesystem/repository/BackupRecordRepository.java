package com.school.teacherarchivesystem.repository;

import com.school.teacherarchivesystem.entity.BackupRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupRecordRepository extends JpaRepository<BackupRecord, Long> {
}
