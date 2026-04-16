package com.crosscutting.starter.queue;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class JobTest {

    @Test
    void defaultConstructorCreatesEmptyJob() {
        Job job = new Job();
        assertThat(job.getId()).isNull();
        assertThat(job.getQueueName()).isNull();
        assertThat(job.getPayload()).isNull();
        assertThat(job.getAttemptCount()).isEqualTo(0);
        assertThat(job.getMaxAttempts()).isEqualTo(0);
    }

    @Test
    void fullConstructorSetsAllFields() {
        Instant created = Instant.now();
        Instant scheduled = created.plusSeconds(60);

        Job job = new Job(42L, "exports", "{\"data\":1}", 2, 5, created, scheduled);

        assertThat(job.getId()).isEqualTo(42L);
        assertThat(job.getQueueName()).isEqualTo("exports");
        assertThat(job.getPayload()).isEqualTo("{\"data\":1}");
        assertThat(job.getAttemptCount()).isEqualTo(2);
        assertThat(job.getMaxAttempts()).isEqualTo(5);
        assertThat(job.getCreatedAt()).isEqualTo(created);
        assertThat(job.getScheduledAt()).isEqualTo(scheduled);
    }

    @Test
    void settersOverrideConstructorValues() {
        Job job = new Job(1L, "q1", "{}", 0, 3, Instant.now(), Instant.now());

        job.setId(99L);
        job.setQueueName("q2");
        job.setPayload("{\"new\":true}");
        job.setAttemptCount(5);
        job.setMaxAttempts(10);

        assertThat(job.getId()).isEqualTo(99L);
        assertThat(job.getQueueName()).isEqualTo("q2");
        assertThat(job.getPayload()).isEqualTo("{\"new\":true}");
        assertThat(job.getAttemptCount()).isEqualTo(5);
        assertThat(job.getMaxAttempts()).isEqualTo(10);
    }
}
