package com.microsaas.experimentengine.domain.repository;

import com.microsaas.experimentengine.domain.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByExperimentId(UUID experimentId);
    List<Event> findByExperimentIdAndEventName(UUID experimentId, String eventName);
}
