package com.microsaas.videonarrator.service;

import com.microsaas.videonarrator.domain.VideoProject;
import com.microsaas.videonarrator.repository.VideoProjectRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoProcessingServiceTest {

    @Mock
    private VideoProjectRepository projectRepository;

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private VideoProcessingService videoProcessingService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(videoProcessingService, "bucketPrefix", "videonarrator-");
    }

    @Test
    void uploadVideo_SavesToMinioAndDB() throws Exception {
        String tenantId = "tenant-1";
        String title = "My Video";
        MockMultipartFile file = new MockMultipartFile("file", "test.mp4", "video/mp4", "data".getBytes());

        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);
        when(projectRepository.save(any(VideoProject.class))).thenAnswer(i -> i.getArguments()[0]);

        VideoProject result = videoProcessingService.uploadVideo(tenantId, title, file);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        assertEquals(title, result.getTitle());
        assertEquals(VideoProject.ProjectStatus.UPLOADED, result.getStatus());

        verify(minioClient).putObject(any(PutObjectArgs.class));
    }
}
