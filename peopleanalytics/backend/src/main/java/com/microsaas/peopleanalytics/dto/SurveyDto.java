package com.microsaas.peopleanalytics.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class SurveyDto {
    private String title;
    private String description;
    private List<Map<String, Object>> questions;
}
