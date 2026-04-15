package com.school.teacherarchivesystem.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {
    @Value("${app.meta.name:teacher-archive-system}")
    private String appName;
    @Value("${app.meta.version:1.0.0}")
    private String appVersion;
    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @GetMapping
    public Map<String, Object> health() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "UP");
        result.put("service", appName);
        result.put("version", appVersion);
        result.put("profile", activeProfile);
        result.put("timestamp", LocalDateTime.now());
        return result;
    }
}
