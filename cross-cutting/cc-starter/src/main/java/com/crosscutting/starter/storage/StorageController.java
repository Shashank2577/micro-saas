package com.crosscutting.starter.storage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cc/storage")
@Tag(name = "Storage", description = "Object storage operations - presigned URLs, file listing, and deletion")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Get a presigned upload URL for putting a file into storage.
     */
    @PostMapping("/upload-url")
    @Operation(summary = "Get presigned upload URL", description = "Generate a time-limited presigned URL for uploading a file to object storage")
    @ApiResponse(responseCode = "200", description = "Upload URL generated")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Storage provider error")
    public Map<String, String> getUploadUrl(@Valid @RequestBody UploadUrlRequest request) {
        String url = storageService.getUploadUrl(
                request.tenantId(),
                request.bucket(),
                request.fileName(),
                request.contentType(),
                request.expiresInSeconds() > 0 ? request.expiresInSeconds() : 3600
        );
        return Map.of("url", url);
    }

    /**
     * Get a presigned download URL for fetching a file from storage.
     */
    @PostMapping("/download-url")
    @Operation(summary = "Get presigned download URL", description = "Generate a time-limited presigned URL for downloading a file from object storage")
    @ApiResponse(responseCode = "200", description = "Download URL generated")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "File not found")
    public Map<String, String> getDownloadUrl(@Valid @RequestBody DownloadUrlRequest request) {
        String url = storageService.getDownloadUrl(
                request.tenantId(),
                request.bucket(),
                request.key(),
                request.expiresInSeconds() > 0 ? request.expiresInSeconds() : 3600
        );
        return Map.of("url", url);
    }

    /**
     * Delete a file from storage.
     */
    @DeleteMapping("/files")
    @Operation(summary = "Delete a file", description = "Permanently remove a file from object storage")
    @ApiResponse(responseCode = "200", description = "File deleted successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "File not found")
    public Map<String, String> deleteFile(@Valid @RequestBody DeleteFileRequest request) {
        storageService.deleteFile(request.tenantId(), request.bucket(), request.key());
        return Map.of("status", "deleted");
    }

    /**
     * List files in a bucket, optionally filtered by prefix.
     */
    @GetMapping("/files")
    @Operation(summary = "List files", description = "List files in a storage bucket, optionally filtered by key prefix")
    @ApiResponse(responseCode = "200", description = "Files listed successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public List<StorageService.FileInfo> listFiles(
            @Parameter(description = "Tenant ID") @RequestParam UUID tenantId,
            @Parameter(description = "Bucket name") @RequestParam String bucket,
            @Parameter(description = "Key prefix filter") @RequestParam(required = false) String prefix) {
        return storageService.listFiles(tenantId, bucket, prefix);
    }

    public record UploadUrlRequest(
            @NotNull(message = "Tenant ID is required") UUID tenantId,
            @NotBlank(message = "Bucket is required") String bucket,
            @NotBlank(message = "File name is required") String fileName,
            @NotBlank(message = "Content type is required") String contentType,
            int expiresInSeconds) {
    }

    public record DownloadUrlRequest(
            @NotNull(message = "Tenant ID is required") UUID tenantId,
            @NotBlank(message = "Bucket is required") String bucket,
            @NotBlank(message = "Key is required") String key,
            int expiresInSeconds) {
    }

    public record DeleteFileRequest(
            @NotNull(message = "Tenant ID is required") UUID tenantId,
            @NotBlank(message = "Bucket is required") String bucket,
            @NotBlank(message = "Key is required") String key) {
    }
}
