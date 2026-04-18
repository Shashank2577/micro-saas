package com.microsaas.auditvault.dto;

import lombok.Data;

@Data
public class EvidenceIngestRequest {
    private String sourceApp;
    private String evidenceType;
    private String content;
    private String url;
}
