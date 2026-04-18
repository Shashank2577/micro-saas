package com.microsaas.integrationbridge.dto;

public class FieldMappingRequest {
    private String sourceField;
    private String targetField;
    private String transformationRule;

    public String getSourceField() { return sourceField; }
    public void setSourceField(String sourceField) { this.sourceField = sourceField; }
    public String getTargetField() { return targetField; }
    public void setTargetField(String targetField) { this.targetField = targetField; }
    public String getTransformationRule() { return transformationRule; }
    public void setTransformationRule(String transformationRule) { this.transformationRule = transformationRule; }
}
