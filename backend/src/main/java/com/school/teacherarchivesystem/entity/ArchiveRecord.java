package com.school.teacherarchivesystem.entity;

import com.school.teacherarchivesystem.enums.ArchiveStatus;
import com.school.teacherarchivesystem.enums.ArchiveType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "archive_record")
public class ArchiveRecord extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private SystemUser teacher;

    @Column(name = "archive_name", nullable = false, length = 100)
    private String archiveName;

    @Enumerated(EnumType.STRING)
    @Column(name = "archive_type", nullable = false, length = 30)
    private ArchiveType archiveType;

    @Column(length = 30)
    private String level;

    @Column(name = "related_date")
    private LocalDate relatedDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ArchiveStatus status;

    @Column(name = "review_comment", length = 300)
    private String reviewComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_by")
    private SystemUser reviewBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(nullable = false)
    private Boolean deleted = false;

    @OneToMany(mappedBy = "archive", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArchiveAttachment> attachments = new ArrayList<>();

    public SystemUser getTeacher() { return teacher; }
    public void setTeacher(SystemUser teacher) { this.teacher = teacher; }
    public String getArchiveName() { return archiveName; }
    public void setArchiveName(String archiveName) { this.archiveName = archiveName; }
    public ArchiveType getArchiveType() { return archiveType; }
    public void setArchiveType(ArchiveType archiveType) { this.archiveType = archiveType; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public LocalDate getRelatedDate() { return relatedDate; }
    public void setRelatedDate(LocalDate relatedDate) { this.relatedDate = relatedDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public ArchiveStatus getStatus() { return status; }
    public void setStatus(ArchiveStatus status) { this.status = status; }
    public String getReviewComment() { return reviewComment; }
    public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }
    public SystemUser getReviewBy() { return reviewBy; }
    public void setReviewBy(SystemUser reviewBy) { this.reviewBy = reviewBy; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    public Boolean getDeleted() { return deleted; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }
    public List<ArchiveAttachment> getAttachments() { return attachments; }
    public void setAttachments(List<ArchiveAttachment> attachments) { this.attachments = attachments; }
}
