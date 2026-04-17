package com.microsaas.documentvault.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class DocumentCreateRequest {
    private String title;
    private String description;
    private UUID folderId;
}
