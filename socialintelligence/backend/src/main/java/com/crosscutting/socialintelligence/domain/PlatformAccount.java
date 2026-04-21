package com.crosscutting.socialintelligence.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "platform_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID tenantId;
    private String platform;
    private String accountName;
    private String accountIdExternal;
    private String accessTokenEncrypted;
    private String refreshTokenEncrypted;
    private LocalDateTime tokenExpiresAt;
    @Builder.Default
    private Boolean isActive = true;
    @Builder.Default
    private LocalDateTime connectedAt = LocalDateTime.now();
}
