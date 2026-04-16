package com.microsaas.hiresignal.repository;

import com.microsaas.hiresignal.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
    List<Candidate> findByRequisitionId(UUID requisitionId);
}
