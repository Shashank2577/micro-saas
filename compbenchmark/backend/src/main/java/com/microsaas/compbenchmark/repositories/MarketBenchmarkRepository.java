package com.microsaas.compbenchmark.repositories;

import com.microsaas.compbenchmark.model.MarketBenchmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MarketBenchmarkRepository extends JpaRepository<MarketBenchmark, UUID> {
    Optional<MarketBenchmark> findByTitleAndLevel(String title, String level);
}
