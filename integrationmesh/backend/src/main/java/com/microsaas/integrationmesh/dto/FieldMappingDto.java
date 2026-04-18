package com.microsaas.integrationmesh.dto;

import lombok.Data;

@Data
public class FieldMappingDto {
    private String sourceField;
    private String targetField;
    private String transformLogic;
}
