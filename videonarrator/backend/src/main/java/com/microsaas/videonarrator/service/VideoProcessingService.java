package com.microsaas.videonarrator.service;

import com.microsaas.videonarrator.domain.VideoProject;
import com.microsaas.videonarrator.repository.VideoProjectRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoProcessingService {

    private final VideoProjectRepository projectRepository;
    private final MinioClient minioClient;

    @Value("${minio.bucket-prefix:videonarrator-}")
    private String bucketPrefix;

    @Transactional
    public VideoProject uploadVideo(String tenantId, String title, MultipartFile file) throws Exception {
        String bucketName = bucketPrefix + tenantId;
        ensureBucketExists(bucketName);

        String objectName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build()
        );

        VideoProject project = new VideoProject();
        project.setTenantId(tenantId);
        project.setTitle(title);
        project.setOriginalFilename(file.getOriginalFilename());
        // For the sake of this autonomous build, we'll construct a direct URL if MinIO is exposed
        project.setVideoUrl("/api/v1/projects/media/" + objectName);
        project.setStoragePath(bucketName + "/" + objectName);
        project.setStatus(VideoProject.ProjectStatus.UPLOADED);

        return projectRepository.save(project);
    }

    public List<VideoProject> getProjects(String tenantId) {
        return projectRepository.findAllByTenantId(tenantId);
    }

    public VideoProject getProject(UUID id, String tenantId) {
        return projectRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Transactional
    public void deleteProject(UUID id, String tenantId) {
        VideoProject project = getProject(id, tenantId);

        try {
            String[] parts = project.getStoragePath().split("/", 2);
            if (parts.length == 2) {
                minioClient.removeObject(
                    RemoveObjectArgs.builder()
                        .bucket(parts[0])
                        .object(parts[1])
                        .build()
                );
            }
        } catch (Exception e) {
            log.error("Failed to delete object from MinIO", e);
        }

        projectRepository.delete(project);
    }

    private void ensureBucketExists(String bucketName) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }
}
