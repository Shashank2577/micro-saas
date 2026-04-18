package com.microsaas.budgetmaster.repository;

import com.microsaas.budgetmaster.domain.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember, UUID> {
    List<FamilyMember> findAllByTenantId(UUID tenantId);
    Optional<FamilyMember> findByIdAndTenantId(UUID id, UUID tenantId);
}
