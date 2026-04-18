package com.microsaas.integrationmesh.dto;

import lombok.Data;
import java.util.Map;
import java.util.UUID;

@Data
public class ConnectorDto {
    private String name;
    private String type;
    private Map<String, Object> config;
}
