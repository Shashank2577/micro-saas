package com.microsaas.documentvault.dto;

import lombok.Data;

@Data
public class DocumentUpdateRequest {
    private String title;
    private String description;
    private String status;
    private Boolean retentionHold;
}
