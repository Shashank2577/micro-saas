package com.microsaas.runwaymodeler.repository;

import com.microsaas.runwaymodeler.model.RunwayProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RunwayProjectionRepository extends JpaRepository<RunwayProjection, UUID> {
}
