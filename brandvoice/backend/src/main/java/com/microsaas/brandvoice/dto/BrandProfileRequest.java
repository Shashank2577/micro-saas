package com.microsaas.brandvoice.dto;

import lombok.Data;

import java.util.List;

@Data
public class BrandProfileRequest {
    private String name;
    private String tone;
    private List<String> vocabularyAllowed;
    private List<String> vocabularyForbidden;
    private List<String> coreValues;
}
