package com.crosscutting.starter.queue;

public interface JobHandler {

    void handle(Job job);

    String getQueueName();
}
