package com.school.teacherarchivesystem.util;

import com.school.teacherarchivesystem.config.AppProperties;
import com.school.teacherarchivesystem.entity.ArchiveAttachment;
import com.school.teacherarchivesystem.entity.SystemUser;
import com.school.teacherarchivesystem.enums.ArchiveType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final AppProperties appProperties;

    public FileStorageService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public ArchiveAttachment storeArchiveFile(SystemUser teacher, ArchiveType archiveType, MultipartFile file) {
        try {
            String uploadPath = appProperties.getStorage().getBasePath();
            Path dir = Path.of(uploadPath, teacher.getId().toString(), archiveType.name());
            Files.createDirectories(dir);

            String originalFileName = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
            String ext = originalFileName.contains(".") ? originalFileName.substring(originalFileName.lastIndexOf('.')) : "";
            String storedFileName = UUID.randomUUID() + ext;
            Path destination = dir.resolve(storedFileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            ArchiveAttachment attachment = new ArchiveAttachment();
            attachment.setOriginalFileName(originalFileName);
            attachment.setStoredFileName(storedFileName);
            attachment.setFilePath(destination.toString());
            attachment.setContentType(file.getContentType());
            attachment.setFileSize(file.getSize());
            return attachment;
        } catch (IOException ex) {
            throw new RuntimeException("文件存储失败：" + ex.getMessage(), ex);
        }
    }

    public Resource loadAsResource(String filePath) {
        try {
            Path path = Path.of(filePath);
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("文件不存在或不可读：" + filePath);
            }
            return resource;
        } catch (MalformedURLException ex) {
            throw new RuntimeException("文件路径无效：" + filePath, ex);
        }
    }

    public String detectContentType(String filePath, String fallback) {
        try {
            String probed = Files.probeContentType(Path.of(filePath));
            if (probed != null) return probed;
        } catch (IOException ignored) {}
        return fallback != null ? fallback : "application/octet-stream";
    }
}
