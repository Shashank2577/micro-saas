package com.microsaas.taskqueue.service;

import com.microsaas.taskqueue.domain.JobPriority;
import com.microsaas.taskqueue.dto.JobRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class EventConsumerService {
    private static final Logger log = LoggerFactory.getLogger(EventConsumerService.class);
    private final JobService jobService;

    public EventConsumerService(JobService jobService) {
        this.jobService = jobService;
    }

    @EventListener
    public void handleTaskQueueEvent(TaskQueueEvent event) {
        if ("taskqueue.job.enqueue".equals(event.getEventType())) {
            try {
                JobRequest req = new JobRequest();
                req.setName((String) event.getPayload().get("name"));
                String priorityStr = (String) event.getPayload().get("priority");
                if (priorityStr != null) {
                    req.setPriority(JobPriority.valueOf(priorityStr));
                }
                req.setPayload((String) event.getPayload().get("payload"));

                log.info("Consumed taskqueue.job.enqueue, creating job {}", req.getName());
                jobService.enqueueJob(req);
            } catch (Exception e) {
                log.error("Failed to process consumed event", e);
            }
        }
    }
}
