package com.microsaas.datagovernanceos.dto;

import lombok.Data;

@Data
public class DataClassificationResult {
    private String classification;
    private Boolean piiStatus;
    private String reasoning;
}
