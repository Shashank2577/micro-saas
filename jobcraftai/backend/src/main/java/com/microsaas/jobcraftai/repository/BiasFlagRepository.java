package com.microsaas.jobcraftai.repository;

import com.microsaas.jobcraftai.model.BiasFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BiasFlagRepository extends JpaRepository<BiasFlag, UUID> {
    List<BiasFlag> findByJobId(UUID jobId);
}
