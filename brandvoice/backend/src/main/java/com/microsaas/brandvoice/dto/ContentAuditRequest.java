package com.microsaas.brandvoice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ContentAuditRequest {
    private UUID brandProfileId;
    private String contentTitle;
    private String contentBody;
    private String channel;
}
