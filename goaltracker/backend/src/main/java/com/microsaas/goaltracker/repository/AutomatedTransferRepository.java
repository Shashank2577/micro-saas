package com.microsaas.goaltracker.repository;

import com.microsaas.goaltracker.entity.AutomatedTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AutomatedTransferRepository extends JpaRepository<AutomatedTransfer, UUID> {
    List<AutomatedTransfer> findByGoalId(UUID goalId);
}
