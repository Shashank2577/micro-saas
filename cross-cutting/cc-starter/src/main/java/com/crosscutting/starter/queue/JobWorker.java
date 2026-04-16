package com.crosscutting.starter.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JobWorker {

    private static final Logger log = LoggerFactory.getLogger(JobWorker.class);
    private static final int DEFAULT_MAX_ATTEMPTS = 3;

    private final QueueService queueService;
    private final Map<String, JobHandler> handlersByQueue;

    public JobWorker(QueueService queueService, List<JobHandler> handlers) {
        this.queueService = queueService;
        this.handlersByQueue = handlers.stream()
                .collect(Collectors.toMap(JobHandler::getQueueName, Function.identity()));
        log.info("JobWorker initialized with handlers for queues: {}", handlersByQueue.keySet());
    }

    @Scheduled(fixedDelayString = "${cc.queue.poll-interval-ms:1000}")
    public void poll() {
        for (String queueName : handlersByQueue.keySet()) {
            pollQueue(queueName);
        }
    }

    private void pollQueue(String queueName) {
        Optional<Job> jobOpt = queueService.dequeue(queueName);
        if (jobOpt.isEmpty()) {
            return;
        }

        Job job = jobOpt.get();
        JobHandler handler = handlersByQueue.get(queueName);

        try {
            handler.handle(job);
            queueService.markComplete(job.getId());
        } catch (Exception e) {
            log.error("Job {} on queue {} failed (attempt {}): {}",
                    job.getId(), queueName, job.getAttemptCount(), e.getMessage(), e);

            int maxAttempts = job.getMaxAttempts() > 0 ? job.getMaxAttempts() : DEFAULT_MAX_ATTEMPTS;
            if (job.getAttemptCount() >= maxAttempts) {
                queueService.moveToDeadLetter(job.getId());
            } else {
                queueService.markRetry(job.getId());
            }
        }
    }
}
