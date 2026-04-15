package com.school.teacherarchivesystem.controller;

import com.school.teacherarchivesystem.common.ApiResponse;
import com.school.teacherarchivesystem.entity.SystemLog;
import com.school.teacherarchivesystem.service.LogService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/logs")
@PreAuthorize("hasRole('ADMIN')")
public class LogController {
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public ApiResponse<?> page(@RequestParam(required = false) String username,
                               @RequestParam(required = false) String module,
                               @RequestParam(required = false) Boolean success,
                               @RequestParam(required = false) LocalDate dateFrom,
                               @RequestParam(required = false) LocalDate dateTo,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(logService.list(username, module, success, dateFrom, dateTo, PageRequest.of(page, size)));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(@RequestParam(required = false) String username,
                                         @RequestParam(required = false) String module,
                                         @RequestParam(required = false) Boolean success,
                                         @RequestParam(required = false) LocalDate dateFrom,
                                         @RequestParam(required = false) LocalDate dateTo) {
        List<SystemLog> rows = logService.export(username, module, success, dateFrom, dateTo);
        String header = "时间,操作人,模块,操作类型,结果,IP,说明\n";
        String body = rows.stream().map(item -> String.join(",",
                csv(item.getCreatedAt() == null ? "" : item.getCreatedAt().toString()),
                csv(item.getUsername()),
                csv(item.getModule()),
                csv(item.getActionType() == null ? "" : item.getActionType().name()),
                csv(Boolean.TRUE.equals(item.getSuccess()) ? "成功" : "失败"),
                csv(item.getIpAddress()),
                csv(item.getDetail())
        )).collect(Collectors.joining("\n"));

        byte[] bytes = ("\ufeff" + header + body).getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=system_logs.csv")
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(bytes);
    }

    private String csv(String value) {
        if (value == null) {
            return "\"\"";
        }
        return "\"" + value.replace("\"", "\"\"")
                .replace("\n", " ")
                .replace("\r", " ") + "\"";
    }
}
