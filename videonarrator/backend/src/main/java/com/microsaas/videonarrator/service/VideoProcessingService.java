package com.microsaas.videonarrator.service;

import com.microsaas.videonarrator.model.VideoProject;
import com.microsaas.videonarrator.repository.VideoProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoProcessingService {

    private final VideoProjectRepository repository;

    @Value("${minio.bucket-prefix:videonarrator-}")
    private String bucketPrefix;

    @Value("${minio.endpoint:http://localhost:9000}")
    private String minioEndpoint;

    @Transactional
    public VideoProject uploadVideo(String tenantId, String title, MultipartFile file) {
        String bucketName = bucketPrefix + tenantId.toLowerCase();
        String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        String storagePath = bucketName + "/" + filename;
        String videoUrl = minioEndpoint + "/" + storagePath;

        VideoProject project = new VideoProject();
        project.setTenantId(tenantId);
        project.setTitle(title);
        project.setOriginalFilename(file.getOriginalFilename());
        project.setStoragePath(storagePath);
        project.setVideoUrl(videoUrl);
        project.setStatus("UPLOADED");
        return repository.save(project);
    }

    public List<VideoProject> getProjects(String tenantId) {
        return repository.findAllByTenantId(tenantId);
    }

    public VideoProject getProject(String tenantId, UUID id) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Transactional
    public void deleteProject(String tenantId, UUID id) {
        VideoProject project = getProject(tenantId, id);
        repository.delete(project);
    }
}
