package com.microsaas.onboardflow.repository;

import com.microsaas.onboardflow.model.ProductivityScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductivityScoreRepository extends JpaRepository<ProductivityScore, UUID> {

    @Query("SELECT AVG(ps.score) FROM ProductivityScore ps")
    Double getAverageProductivityScore();
}
