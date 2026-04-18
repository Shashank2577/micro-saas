package com.microsaas.usageintelligence.dto;

import java.util.Map;

public class CreateCohortDto {
    private String name;
    private String description;
    private Map<String, Object> criteria;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Map<String, Object> getCriteria() { return criteria; }
    public void setCriteria(Map<String, Object> criteria) { this.criteria = criteria; }
}
