package com.microsaas.dataroomai.repository;

import com.microsaas.dataroomai.domain.DataRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DataRoomRepository extends JpaRepository<DataRoom, UUID> {
    List<DataRoom> findByTenantId(UUID tenantId);
    Optional<DataRoom> findByIdAndTenantId(UUID id, UUID tenantId);
}
