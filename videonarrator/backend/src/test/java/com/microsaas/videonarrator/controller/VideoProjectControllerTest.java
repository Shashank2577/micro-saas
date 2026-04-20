package com.microsaas.videonarrator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.videonarrator.domain.VideoProject;
import com.microsaas.videonarrator.service.VideoProcessingService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VideoProjectController.class,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration"
    })
@AutoConfigureMockMvc(addFilters = false)
class VideoProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoProcessingService videoProcessingService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @Test
    void createProject_ReturnsProject() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.mp4", "video/mp4", "test video content".getBytes());

        VideoProject project = new VideoProject();
        project.setId(UUID.randomUUID());
        project.setTitle("Test Video");
        project.setOriginalFilename("test.mp4");
        project.setTenantId(tenantId.toString());

        when(videoProcessingService.uploadVideo(eq(tenantId.toString()), eq("Test Video"), any()))
                .thenReturn(project);

        mockMvc.perform(multipart("/api/v1/projects")
                        .file(file)
                        .param("title", "Test Video")
                        .header("X-Tenant-ID", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Video"));
    }

    @Test
    void getProjects_ReturnsList() throws Exception {
        VideoProject project = new VideoProject();
        project.setId(UUID.randomUUID());
        project.setTitle("Test Video");

        when(videoProcessingService.getProjects(tenantId.toString())).thenReturn(List.of(project));

        mockMvc.perform(get("/api/v1/projects")
                        .header("X-Tenant-ID", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Video"));
    }
}
