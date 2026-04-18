package com.microsaas.taskqueue.domain;

public enum JobStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED,
    CANCELLED,
    DEAD_LETTER
}
