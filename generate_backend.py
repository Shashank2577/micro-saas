import os

base_pkg = 'socialintelligence/backend/src/main/java/com/crosscutting/socialintelligence'
domain_dir = f'{base_pkg}/domain'
repo_dir = f'{base_pkg}/repository'
service_dir = f'{base_pkg}/service'
controller_dir = f'{base_pkg}/controller'
dto_dir = f'{base_pkg}/dto'

os.makedirs(domain_dir, exist_ok=True)
os.makedirs(repo_dir, exist_ok=True)
os.makedirs(service_dir, exist_ok=True)
os.makedirs(controller_dir, exist_ok=True)
os.makedirs(dto_dir, exist_ok=True)

# Delete old domain entities that don't match
for f in os.listdir(domain_dir):
    if f.endswith('.java'):
        os.remove(os.path.join(domain_dir, f))

# 1. PlatformAccount
with open(f'{domain_dir}/PlatformAccount.java', 'w') as f:
    f.write('''package com.crosscutting.socialintelligence.domain;

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
''')

# 2. EngagementMetric
with open(f'{domain_dir}/EngagementMetric.java', 'w') as f:
    f.write('''package com.crosscutting.socialintelligence.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "engagement_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EngagementMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID tenantId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private PlatformAccount account;
    private LocalDate metricDate;
    private Long followersCount;
    private Long impressions;
    private Long reach;
    private Long likes;
    private Long comments;
    private Long shares;
    private BigDecimal engagementRate;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
''')

# 3. ContentPost
with open(f'{domain_dir}/ContentPost.java', 'w') as f:
    f.write('''package com.crosscutting.socialintelligence.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "content_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentPost {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID tenantId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private PlatformAccount account;
    private String externalPostId;
    private String contentType;
    private String caption;
    private LocalDateTime postedAt;
    private Long likes;
    private Long comments;
    private Long shares;
    private Long views;
    private BigDecimal engagementRate;
}
''')

# 4. AudienceDemographic
with open(f'{domain_dir}/AudienceDemographic.java', 'w') as f:
    f.write('''package com.crosscutting.socialintelligence.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audience_demographics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AudienceDemographic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID tenantId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private PlatformAccount account;
    private String ageRange;
    private String gender;
    private String country;
    private BigDecimal percentage;
    @Builder.Default
    private LocalDateTime recordedAt = LocalDateTime.now();
}
''')

# 5. GrowthRecommendation
with open(f'{domain_dir}/GrowthRecommendation.java', 'w') as f:
    f.write('''package com.crosscutting.socialintelligence.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "growth_recommendations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrowthRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID tenantId;
    private String recommendationText;
    private String platform;
    private String category;
    private Integer priority;
    @Builder.Default
    private LocalDateTime generatedAt = LocalDateTime.now();
    @Builder.Default
    private Boolean isActioned = false;
}
''')
