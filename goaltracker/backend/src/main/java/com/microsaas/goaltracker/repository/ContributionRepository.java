package com.microsaas.goaltracker.repository;

import com.microsaas.goaltracker.entity.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, UUID> {
    List<Contribution> findByGoalId(UUID goalId);
}
