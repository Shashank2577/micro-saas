package com.microsaas.compbenchmark.repositories;

import com.microsaas.compbenchmark.model.CompGap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompGapRepository extends JpaRepository<CompGap, UUID> {
}
