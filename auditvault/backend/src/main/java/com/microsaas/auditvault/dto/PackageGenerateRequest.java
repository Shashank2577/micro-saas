package com.microsaas.auditvault.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class PackageGenerateRequest {
    private UUID frameworkId;
    private String name;
}
