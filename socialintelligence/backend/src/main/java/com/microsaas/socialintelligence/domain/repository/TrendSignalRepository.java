package com.microsaas.socialintelligence.domain.repository;

import com.microsaas.socialintelligence.domain.model.TrendSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrendSignalRepository extends JpaRepository<TrendSignal, UUID> {
    List<TrendSignal> findByStatus(String status);
}
