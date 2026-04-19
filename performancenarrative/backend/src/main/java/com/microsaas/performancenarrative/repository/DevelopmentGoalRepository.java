package com.microsaas.performancenarrative.repository;

import com.microsaas.performancenarrative.entity.DevelopmentGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public interface DevelopmentGoalRepository extends JpaRepository<DevelopmentGoal, UUID> {
}
