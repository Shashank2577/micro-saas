package com.microsaas.incidentbrain.domain.repository;

import com.microsaas.incidentbrain.domain.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, String> {
}
