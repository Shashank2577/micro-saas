package com.microsaas.authvault.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class MfaVerifyRequest {
    private UUID userId;
    private String code;
}
