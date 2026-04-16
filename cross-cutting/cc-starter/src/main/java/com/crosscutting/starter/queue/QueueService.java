package com.crosscutting.starter.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QueueService {

    private static final Logger log = LoggerFactory.getLogger(QueueService.class);

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Job> JOB_ROW_MAPPER = (rs, rowNum) -> new Job(
            rs.getLong("id"),
            rs.getString("queue_name"),
            rs.getString("payload"),
            rs.getInt("attempts"),
            rs.getInt("max_attempts"),
            rs.getTimestamp("created_at").toInstant(),
            rs.getTimestamp("scheduled_at").toInstant()
    );

    public QueueService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void enqueue(String queueName, String payload, int delaySeconds) {
        jdbcTemplate.update(
                "INSERT INTO cc.job_queue (queue_name, payload, status, scheduled_at) " +
                        "VALUES (?, ?::jsonb, 'pending', NOW() + (? || ' seconds')::interval)",
                queueName, payload, String.valueOf(delaySeconds)
        );
        log.debug("Enqueued job on queue={} with delay={}s", queueName, delaySeconds);
    }

    public Optional<Job> dequeue(String queueName) {
        List<Job> jobs = jdbcTemplate.query(
                "UPDATE cc.job_queue SET status = 'processing', started_at = NOW(), " +
                        "attempts = attempts + 1 " +
                        "WHERE id = (SELECT id FROM cc.job_queue " +
                        "WHERE queue_name = ? AND status IN ('pending', 'retry') " +
                        "AND scheduled_at <= NOW() " +
                        "ORDER BY scheduled_at ASC LIMIT 1 FOR UPDATE SKIP LOCKED) " +
                        "RETURNING *",
                JOB_ROW_MAPPER,
                queueName
        );
        return jobs.stream().findFirst();
    }

    public void markComplete(Long jobId) {
        jdbcTemplate.update(
                "UPDATE cc.job_queue SET status = 'completed', completed_at = NOW() WHERE id = ?",
                jobId
        );
        log.debug("Marked job {} as completed", jobId);
    }

    public void markFailed(Long jobId, String error) {
        jdbcTemplate.update(
                "UPDATE cc.job_queue SET status = 'failed', error = ? WHERE id = ?",
                error, jobId
        );
        log.debug("Marked job {} as failed: {}", jobId, error);
    }

    public void markRetry(Long jobId) {
        jdbcTemplate.update(
                "UPDATE cc.job_queue SET status = 'retry', started_at = NULL WHERE id = ?",
                jobId
        );
        log.debug("Marked job {} for retry", jobId);
    }

    public void moveToDeadLetter(Long jobId) {
        jdbcTemplate.update(
                "UPDATE cc.job_queue SET status = 'dead_letter' WHERE id = ?",
                jobId
        );
        log.warn("Moved job {} to dead letter queue", jobId);
    }

    public List<Map<String, Object>> getQueueStats() {
        return jdbcTemplate.queryForList(
                "SELECT queue_name, status, COUNT(*) as count " +
                        "FROM cc.job_queue GROUP BY queue_name, status ORDER BY queue_name, status"
        );
    }

    public List<Job> getDeadLetterJobs(String queueName) {
        return jdbcTemplate.query(
                "SELECT * FROM cc.job_queue WHERE queue_name = ? AND status = 'dead_letter' " +
                        "ORDER BY created_at DESC",
                JOB_ROW_MAPPER,
                queueName
        );
    }

    public void retryDeadLetterJob(Long jobId) {
        jdbcTemplate.update(
                "UPDATE cc.job_queue SET status = 'pending', attempts = 0, error = NULL, " +
                        "started_at = NULL, completed_at = NULL, scheduled_at = NOW() WHERE id = ? AND status = 'dead_letter'",
                jobId
        );
        log.info("Retried dead letter job {}", jobId);
    }
}
