package com.microsaas.documentvault.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShareRequest {
    private String email;
    private String accessLevel;
    private LocalDateTime expiresAt;
    private String password;
}
