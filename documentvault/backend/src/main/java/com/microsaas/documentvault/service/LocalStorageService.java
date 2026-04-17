package com.microsaas.documentvault.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import java.io.IOException;

@Service
public class LocalStorageService {
    public String store(MultipartFile file, UUID tenantId) throws IOException {
        // Mock S3 storage, returning mock S3 key
        return "s3://mock-bucket/" + tenantId + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
    }
    
    public byte[] retrieve(String s3Key) {
        // Mock retrieval
        return "mock file content".getBytes();
    }
}
