package com.microsaas.documentintelligence.dto;

import lombok.Data;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class DocumentDTO {
    private UUID id;
    private String filename;
    private String contentType;
    private String status;
    private String documentType;
    private Long sizeBytes;
    private ZonedDateTime uploadedAt;
}
