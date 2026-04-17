package com.crosscutting.starter.storage;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class StorageService {

    private static final Logger log = LoggerFactory.getLogger(StorageService.class);

    private final MinioClient minioClient;

    public StorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Ensure the bucket exists in MinIO, creating it if necessary.
     */
    private void ensureBucketExists(String bucket) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                log.info("Created storage bucket: {}", bucket);
            }
        } catch (Exception e) {
            log.warn("Failed to ensure bucket exists: {}", bucket, e);
        }
    }

    /**
     * Generate a presigned PUT URL for uploading a file.
     * The object key follows the pattern: {tenantId}/{bucket}/{fileName}
     */
    public String getUploadUrl(UUID tenantId, String bucket, String fileName,
                               String contentType, int expiresInSeconds) {
        ensureBucketExists(bucket);
        String objectKey = tenantId + "/" + bucket + "/" + fileName;
        try {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucket)
                            .object(objectKey)
                            .expiry(expiresInSeconds, TimeUnit.SECONDS)
                            .extraHeaders(Map.of("Content-Type", contentType))
                            .build()
            );
            log.info("Generated presigned upload URL for tenant={}, bucket={}, file={}",
                    tenantId, bucket, fileName);
            return url;
        } catch (Exception e) {
            throw new StorageException("Failed to generate upload URL", e);
        }
    }

    /**
     * Generate a presigned GET URL for downloading a file.
     */
    public String getDownloadUrl(UUID tenantId, String bucket, String key, int expiresInSeconds) {
        ensureBucketExists(bucket);
        String objectKey = tenantId + "/" + bucket + "/" + key;
        try {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(objectKey)
                            .expiry(expiresInSeconds, TimeUnit.SECONDS)
                            .build()
            );
            log.info("Generated presigned download URL for tenant={}, bucket={}, key={}",
                    tenantId, bucket, key);
            return url;
        } catch (Exception e) {
            throw new StorageException("Failed to generate download URL", e);
        }
    }

    /**
     * Delete a file from storage.
     */
    public void deleteFile(UUID tenantId, String bucket, String key) {
        String objectKey = tenantId + "/" + bucket + "/" + key;
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .build()
            );
            log.info("Deleted file tenant={}, bucket={}, key={}", tenantId, bucket, key);
        } catch (Exception e) {
            throw new StorageException("Failed to delete file", e);
        }
    }

    /**
     * List files in a bucket with an optional prefix, scoped to a tenant.
     */
    public List<FileInfo> listFiles(UUID tenantId, String bucket, String prefix) {
        String objectPrefix = tenantId + "/" + bucket + "/";
        if (prefix != null && !prefix.isBlank()) {
            objectPrefix += prefix;
        }
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucket)
                            .prefix(objectPrefix)
                            .build()
            );
            List<FileInfo> files = new ArrayList<>();
            for (Result<Item> result : results) {
                Item item = result.get();
                files.add(new FileInfo(
                        item.objectName(),
                        item.size(),
                        item.isDir(),
                        item.lastModified() != null ? item.lastModified().toString() : null
                ));
            }
            log.debug("Listed {} files for tenant={}, bucket={}, prefix={}",
                    files.size(), tenantId, bucket, prefix);
            return files;
        } catch (Exception e) {
            throw new StorageException("Failed to list files", e);
        }
    }

    public record FileInfo(String key, long size, boolean directory, String lastModified) {
    }

    public static class StorageException extends RuntimeException {
        public StorageException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
