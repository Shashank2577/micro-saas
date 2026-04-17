package com.crosscutting.starter.ai;

public record ChatResponse(
        String id,
        String model,
        String content,
        Usage usage
) {

    public record Usage(int promptTokens, int completionTokens, int totalTokens) {
    }
}
