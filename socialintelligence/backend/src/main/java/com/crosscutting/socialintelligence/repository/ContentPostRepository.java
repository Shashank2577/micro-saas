package com.crosscutting.socialintelligence.repository;

import com.crosscutting.socialintelligence.domain.ContentPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContentPostRepository extends JpaRepository<ContentPost, UUID> {
    List<ContentPost> findByTenantId(UUID tenantId);
}
