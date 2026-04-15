package com.school.teacherarchivesystem.repository;

import com.school.teacherarchivesystem.entity.ArchiveAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveAttachmentRepository extends JpaRepository<ArchiveAttachment, Long> {
}
