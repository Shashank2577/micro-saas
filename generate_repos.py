import os

base_pkg = 'socialintelligence/backend/src/main/java/com/crosscutting/socialintelligence/repository'

repos = [
    ('PlatformAccount', 'PlatformAccountRepository'),
    ('EngagementMetric', 'EngagementMetricRepository'),
    ('ContentPost', 'ContentPostRepository'),
    ('AudienceDemographic', 'AudienceDemographicRepository'),
    ('GrowthRecommendation', 'GrowthRecommendationRepository')
]

for entity, repo in repos:
    with open(f'{base_pkg}/{repo}.java', 'w') as f:
        f.write(f'''package com.crosscutting.socialintelligence.repository;

import com.crosscutting.socialintelligence.domain.{entity};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface {repo} extends JpaRepository<{entity}, UUID> {{
    List<{entity}> findByTenantId(UUID tenantId);
}}
''')
