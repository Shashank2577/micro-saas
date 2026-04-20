package com.microsaas.onboardflow.dto;

import lombok.Data;
import java.util.Map;

@Data
public class SystemProvisioningRequestRequest {
    private String name;
    private String status;
    private Map<String, Object> metadataJson;
}
