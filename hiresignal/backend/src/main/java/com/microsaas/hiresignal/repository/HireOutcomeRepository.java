package com.microsaas.hiresignal.repository;

import com.microsaas.hiresignal.model.HireOutcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HireOutcomeRepository extends JpaRepository<HireOutcome, UUID> {
}
