package com.microsaas.documentintelligence.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class DocumentExtractionDTO {
    private UUID id;
    private String extractionType;
    private String keyName;
    private String valueText;
    private BigDecimal confidenceScore;
    private Integer pageNumber;
}
