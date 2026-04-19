package com.microsaas.hiresignal.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HireSignalEventListener {

    @KafkaListener(topics = "jobcraftai.posting.published", groupId = "hiresignal-group")
    public void consumeJobPublished(Map<String, Object> event) {
        log.info("Consumed event from jobcraftai.posting.published: {}", event.get("event_id"));
        // Process job publication
    }

    @KafkaListener(topics = "interviewos.score.submitted", groupId = "hiresignal-group")
    public void consumeScoreSubmitted(Map<String, Object> event) {
        log.info("Consumed event from interviewos.score.submitted: {}", event.get("event_id"));
        // Process score submission
    }

    @KafkaListener(topics = "peopleanalytics.org.signal.updated", groupId = "hiresignal-group")
    public void consumeSignalUpdated(Map<String, Object> event) {
        log.info("Consumed event from peopleanalytics.org.signal.updated: {}", event.get("event_id"));
        // Process signal update
    }
}
