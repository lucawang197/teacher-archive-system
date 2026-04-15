package com.school.teacherarchivesystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.teacherarchivesystem.dto.auth.LoginRequest;
import com.school.teacherarchivesystem.dto.evaluation.ScoringRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthAndScoringIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private String login(String username, String password) throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);
        String response = mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).path("data").path("token").asText();
    }

    @Test
    void shouldLoginAndSeeDashboard() throws Exception {
        mockMvc.perform(get("/dashboard/summary")
                        .header("Authorization", "Bearer " + login("admin", "123456")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.currentUser").exists());
    }

    @Test
    void shouldUploadArchiveThenAdminRunScoring() throws Exception {
        String teacherToken = login("zhang_san", "123456");
        String adminToken = login("admin", "123456");

        String formJson = "{\"archiveName\":\"校级公开课\",\"archiveType\":\"OPEN_CLASS\","
                + "\"level\":\"校级\",\"relatedDate\":\"2026-03-01\",\"description\":\"语文公开课示例\"}";
        MockMultipartFile form = new MockMultipartFile("form", "", "application/json", formJson.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file = new MockMultipartFile("files", "demo.pdf", "application/pdf", "fake-pdf-content".getBytes());

        String uploadResponse = mockMvc.perform(
                multipart("/archives")
                        .file(form)
                        .file(file)
                        .header("Authorization", "Bearer " + teacherToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        long archiveId = objectMapper.readTree(uploadResponse).path("data").path("id").asLong();

        mockMvc.perform(post("/archives/" + archiveId + "/review")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"approved\":true,\"reviewComment\":\"通过\"}"))
                .andExpect(status().isOk());

        ScoringRequest scoringRequest = new ScoringRequest();
        scoringRequest.setTemplateId(1L);
        mockMvc.perform(post("/evaluations/run")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scoringRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        mockMvc.perform(get("/exports/results")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString(".xlsx")));
    }
}
