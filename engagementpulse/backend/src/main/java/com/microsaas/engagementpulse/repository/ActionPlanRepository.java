package com.microsaas.engagementpulse.repository;

import com.microsaas.engagementpulse.model.ActionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ActionPlanRepository extends JpaRepository<ActionPlan, UUID> {
}
