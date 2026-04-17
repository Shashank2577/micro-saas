package com.crosscutting.starter.storage;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private StorageService storageService;

    private final UUID tenantId = UUID.randomUUID();

    @Test
    void getUploadUrl_returnsPresignedPutUrl() throws Exception {
        String expectedUrl = "https://minio.example.com/presigned-put";
        when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class)))
                .thenReturn(expectedUrl);

        String url = storageService.getUploadUrl(tenantId, "documents", "report.pdf",
                "application/pdf", 3600);

        assertThat(url).isEqualTo(expectedUrl);
        ArgumentCaptor<GetPresignedObjectUrlArgs> captor =
                ArgumentCaptor.forClass(GetPresignedObjectUrlArgs.class);
        verify(minioClient).getPresignedObjectUrl(captor.capture());
        GetPresignedObjectUrlArgs args = captor.getValue();
        assertThat(args.bucket()).isEqualTo("documents");
        assertThat(args.object()).isEqualTo(tenantId + "/documents/report.pdf");
    }

    @Test
    void getUploadUrl_throwsStorageExceptionOnFailure() throws Exception {
        when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class)))
                .thenThrow(new RuntimeException("Connection refused"));

        assertThatThrownBy(() -> storageService.getUploadUrl(tenantId, "docs", "f.txt",
                "text/plain", 300))
                .isInstanceOf(StorageService.StorageException.class)
                .hasMessageContaining("Failed to generate upload URL");
    }

    @Test
    void getDownloadUrl_returnsPresignedGetUrl() throws Exception {
        String expectedUrl = "https://minio.example.com/presigned-get";
        when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class)))
                .thenReturn(expectedUrl);

        String url = storageService.getDownloadUrl(tenantId, "documents", "report.pdf", 3600);

        assertThat(url).isEqualTo(expectedUrl);
        ArgumentCaptor<GetPresignedObjectUrlArgs> captor =
                ArgumentCaptor.forClass(GetPresignedObjectUrlArgs.class);
        verify(minioClient).getPresignedObjectUrl(captor.capture());
        GetPresignedObjectUrlArgs args = captor.getValue();
        assertThat(args.bucket()).isEqualTo("documents");
        assertThat(args.object()).isEqualTo(tenantId + "/documents/report.pdf");
    }

    @Test
    void getDownloadUrl_throwsStorageExceptionOnFailure() throws Exception {
        when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class)))
                .thenThrow(new RuntimeException("Timeout"));

        assertThatThrownBy(() -> storageService.getDownloadUrl(tenantId, "docs", "f.txt", 300))
                .isInstanceOf(StorageService.StorageException.class)
                .hasMessageContaining("Failed to generate download URL");
    }

    @Test
    void deleteFile_removesObjectFromMinio() throws Exception {
        storageService.deleteFile(tenantId, "documents", "report.pdf");

        ArgumentCaptor<RemoveObjectArgs> captor = ArgumentCaptor.forClass(RemoveObjectArgs.class);
        verify(minioClient).removeObject(captor.capture());
        RemoveObjectArgs args = captor.getValue();
        assertThat(args.bucket()).isEqualTo("documents");
        assertThat(args.object()).isEqualTo(tenantId + "/documents/report.pdf");
    }

    @Test
    void deleteFile_throwsStorageExceptionOnFailure() throws Exception {
        doThrow(new RuntimeException("Access denied"))
                .when(minioClient).removeObject(any(RemoveObjectArgs.class));

        assertThatThrownBy(() -> storageService.deleteFile(tenantId, "docs", "f.txt"))
                .isInstanceOf(StorageService.StorageException.class)
                .hasMessageContaining("Failed to delete file");
    }

    @Test
    void listFiles_returnsFileInfoList() throws Exception {
        // MinIO listObjects returns an Iterable<Result<Item>>, which is difficult to mock
        // deeply. We verify the call is made with correct args.
        when(minioClient.listObjects(any(ListObjectsArgs.class)))
                .thenReturn(List.of());

        List<StorageService.FileInfo> files = storageService.listFiles(tenantId, "documents", "reports/");

        assertThat(files).isEmpty();
        ArgumentCaptor<ListObjectsArgs> captor = ArgumentCaptor.forClass(ListObjectsArgs.class);
        verify(minioClient).listObjects(captor.capture());
        ListObjectsArgs args = captor.getValue();
        assertThat(args.bucket()).isEqualTo("documents");
        assertThat(args.prefix()).isEqualTo(tenantId + "/documents/reports/");
    }

    @Test
    void listFiles_withNullPrefix_usesBasePrefix() throws Exception {
        when(minioClient.listObjects(any(ListObjectsArgs.class)))
                .thenReturn(List.of());

        storageService.listFiles(tenantId, "documents", null);

        ArgumentCaptor<ListObjectsArgs> captor = ArgumentCaptor.forClass(ListObjectsArgs.class);
        verify(minioClient).listObjects(captor.capture());
        ListObjectsArgs args = captor.getValue();
        assertThat(args.prefix()).isEqualTo(tenantId + "/documents/");
    }

    @Test
    void listFiles_throwsStorageExceptionOnFailure() {
        when(minioClient.listObjects(any(ListObjectsArgs.class)))
                .thenThrow(new RuntimeException("Network error"));

        assertThatThrownBy(() -> storageService.listFiles(tenantId, "docs", null))
                .isInstanceOf(StorageService.StorageException.class)
                .hasMessageContaining("Failed to list files");
    }
}
