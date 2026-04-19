package com.microsaas.videonarrator.service;

import com.microsaas.videonarrator.model.VideoProject;
import com.microsaas.videonarrator.repository.VideoProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VideoProcessingServiceTest {

    @Mock
    private VideoProjectRepository repository;

    @InjectMocks
    private VideoProcessingService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "bucketPrefix", "videonarrator-");
        ReflectionTestUtils.setField(service, "minioEndpoint", "http://localhost:9000");
    }

    @Test
    void testUploadVideo() {
        MultipartFile file = new MockMultipartFile("file", "test.mp4", "video/mp4", "content".getBytes());
        VideoProject saved = new VideoProject();
        saved.setId(UUID.randomUUID());
        saved.setTenantId("tenant1");
        saved.setTitle("My Video");

        when(repository.save(any(VideoProject.class))).thenReturn(saved);

        VideoProject result = service.uploadVideo("tenant1", "My Video", file);

        assertNotNull(result);
        assertEquals("tenant1", result.getTenantId());
        verify(repository, times(1)).save(any(VideoProject.class));
    }

    @Test
    void testGetProject() {
        UUID id = UUID.randomUUID();
        VideoProject project = new VideoProject();
        project.setId(id);

        when(repository.findByIdAndTenantId(id, "tenant1")).thenReturn(Optional.of(project));

        VideoProject result = service.getProject("tenant1", id);
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testDeleteProject() {
        UUID id = UUID.randomUUID();
        VideoProject project = new VideoProject();
        project.setId(id);

        when(repository.findByIdAndTenantId(id, "tenant1")).thenReturn(Optional.of(project));
        doNothing().when(repository).delete(project);

        service.deleteProject("tenant1", id);

        verify(repository, times(1)).delete(project);
    }
}
