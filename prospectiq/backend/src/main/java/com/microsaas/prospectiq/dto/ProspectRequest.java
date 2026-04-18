package com.microsaas.prospectiq.dto;

import lombok.Data;

@Data
public class ProspectRequest {
    private String name;
    private String domain;
    private String industry;
    private String region;
    private String crmId;
}
