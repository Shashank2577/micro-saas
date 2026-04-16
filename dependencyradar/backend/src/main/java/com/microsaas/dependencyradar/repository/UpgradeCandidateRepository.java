package com.microsaas.dependencyradar.repository;

import com.microsaas.dependencyradar.model.UpgradeCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpgradeCandidateRepository extends JpaRepository<UpgradeCandidate, Long> {
    List<UpgradeCandidate> findByDependencyId(Long dependencyId);
}
