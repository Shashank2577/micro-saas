package com.microsaas.prospectiq.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ICPProfileRequest {
    private String name;
    private Map<String, Object> criteria;
}
