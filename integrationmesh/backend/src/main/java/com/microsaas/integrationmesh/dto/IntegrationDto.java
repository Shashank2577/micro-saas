package com.microsaas.integrationmesh.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class IntegrationDto {
    private String name;
    private UUID sourceConnectorId;
    private UUID targetConnectorId;
}
