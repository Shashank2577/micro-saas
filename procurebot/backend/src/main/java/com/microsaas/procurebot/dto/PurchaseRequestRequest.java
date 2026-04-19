package com.microsaas.procurebot.dto;

import lombok.Data;
import java.util.Map;

@Data
public class PurchaseRequestRequest {
    private String name;
    private String status;
    private Map<String, Object> metadataJson;
}
