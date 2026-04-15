package com.school.teacherarchivesystem.dto.archive;

import com.school.teacherarchivesystem.enums.ArchiveType;

import java.time.LocalDate;

public class ArchiveFormRequest {
    private String archiveName;
    private ArchiveType archiveType;
    private String level;
    private LocalDate relatedDate;
    private String description;

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
}
