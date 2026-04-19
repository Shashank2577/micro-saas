package com.microsaas.cashflowai.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ScenarioRunDto {
    private UUID id;
    private String name;
    private String status;
    private String metadataJson;
}
