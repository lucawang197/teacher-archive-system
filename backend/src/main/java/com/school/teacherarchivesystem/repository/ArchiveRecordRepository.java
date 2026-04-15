package com.school.teacherarchivesystem.repository;

import com.school.teacherarchivesystem.entity.ArchiveRecord;
import com.school.teacherarchivesystem.enums.ArchiveStatus;
import com.school.teacherarchivesystem.enums.ArchiveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ArchiveRecordRepository extends JpaRepository<ArchiveRecord, Long>, JpaSpecificationExecutor<ArchiveRecord> {
    long countByStatusAndDeletedFalse(ArchiveStatus status);
    long countByDeletedFalse();
    long countByTeacherIdAndDeletedFalse(Long teacherId);
    long countByTeacherIdAndArchiveTypeAndStatusAndDeletedFalse(Long teacherId, ArchiveType archiveType, ArchiveStatus status);
}
