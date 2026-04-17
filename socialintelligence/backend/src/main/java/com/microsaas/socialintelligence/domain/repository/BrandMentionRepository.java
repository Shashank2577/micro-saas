package com.microsaas.socialintelligence.domain.repository;

import com.microsaas.socialintelligence.domain.model.BrandMention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface BrandMentionRepository extends JpaRepository<BrandMention, UUID> {

    @Query("SELECT b FROM BrandMention b WHERE " +
           "(:sentiment IS NULL OR b.sentiment = :sentiment) AND " +
           "(cast(:startDate as timestamp) IS NULL OR b.detectedAt >= :startDate) AND " +
           "(cast(:endDate as timestamp) IS NULL OR b.detectedAt <= :endDate)")
    List<BrandMention> findWithFilters(@Param("sentiment") String sentiment,
                                       @Param("startDate") Instant startDate,
                                       @Param("endDate") Instant endDate);
}
