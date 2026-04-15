package com.school.teacherarchivesystem.controller;

import com.school.teacherarchivesystem.common.BusinessException;
import com.school.teacherarchivesystem.entity.ArchiveAttachment;
import com.school.teacherarchivesystem.repository.ArchiveAttachmentRepository;
import com.school.teacherarchivesystem.util.FileStorageService;
import com.school.teacherarchivesystem.util.SecurityUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/attachments")
public class AttachmentController {
    private final ArchiveAttachmentRepository attachmentRepository;
    private final FileStorageService fileStorageService;

    public AttachmentController(ArchiveAttachmentRepository attachmentRepository, FileStorageService fileStorageService) {
        this.attachmentRepository = attachmentRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        ArchiveAttachment attachment = findAccessibleAttachment(id);
        Resource resource = fileStorageService.loadAsResource(attachment.getFilePath());
        String contentType = fileStorageService.detectContentType(attachment.getFilePath(), attachment.getContentType());
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(attachment.getOriginalFileName(), StandardCharsets.UTF_8)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<Resource> preview(@PathVariable Long id) {
        ArchiveAttachment attachment = findAccessibleAttachment(id);
        Resource resource = fileStorageService.loadAsResource(attachment.getFilePath());
        String contentType = fileStorageService.detectContentType(attachment.getFilePath(), attachment.getContentType());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    private ArchiveAttachment findAccessibleAttachment(Long id) {
        ArchiveAttachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("附件不存在"));
        if (attachment.getArchive() == null || Boolean.TRUE.equals(attachment.getArchive().getDeleted())) {
            throw new BusinessException("附件所属档案不存在");
        }
        if (!SecurityUtils.isAdmin() && !attachment.getArchive().getTeacher().getId().equals(SecurityUtils.currentUser().getUserId())) {
            throw new BusinessException("无权访问该附件");
        }
        return attachment;
    }
}
