package com.micro.interviewos.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class InterviewEventListener {

    private static final Logger log = LoggerFactory.getLogger(InterviewEventListener.class);

    @EventListener(condition = "#event.eventType eq 'hiresignal.candidate.shortlisted'")
    public void handleCandidateShortlisted(InterviewEvent event) {
        log.info("Handling candidate shortlisted event: {}", event.getEventId());
    }

    @EventListener(condition = "#event.eventType eq 'jobcraftai.posting.published'")
    public void handlePostingPublished(InterviewEvent event) {
        log.info("Handling posting published event: {}", event.getEventId());
    }

    @EventListener(condition = "#event.eventType eq 'performancenarrative.review.finalized'")
    public void handleReviewFinalized(InterviewEvent event) {
        log.info("Handling review finalized event: {}", event.getEventId());
    }
}
