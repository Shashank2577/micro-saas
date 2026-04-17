package com.crosscutting.starter.queue;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cc/queues")
@Tag(name = "Queues", description = "Queue monitoring and dead-letter job management")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping("/stats")
    @Operation(summary = "Get queue statistics", description = "Return depth and processing stats for all registered queues")
    @ApiResponse(responseCode = "200", description = "Queue stats returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public List<Map<String, Object>> getStats() {
        return queueService.getQueueStats();
    }

    @GetMapping("/{name}/dead-letter")
    @Operation(summary = "List dead-letter jobs", description = "Retrieve all failed jobs in the dead-letter queue for a specific queue")
    @ApiResponse(responseCode = "200", description = "Dead-letter jobs returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Queue not found")
    public List<Job> getDeadLetterJobs(@Parameter(description = "Queue name") @PathVariable String name) {
        return queueService.getDeadLetterJobs(name);
    }

    @PostMapping("/{name}/dead-letter/{id}/retry")
    @Operation(summary = "Retry a dead-letter job", description = "Re-enqueue a failed job from the dead-letter queue for reprocessing")
    @ApiResponse(responseCode = "200", description = "Job re-enqueued for retry")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Job not found")
    public void retryDeadLetterJob(
            @Parameter(description = "Queue name") @PathVariable String name,
            @Parameter(description = "Dead-letter job ID") @PathVariable Long id) {
        queueService.retryDeadLetterJob(id);
    }
}
