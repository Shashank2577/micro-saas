package com.crosscutting.starter.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ChatRequest(
        @NotBlank(message = "Model is required") String model,
        @NotEmpty(message = "Messages must not be empty") List<ChatMessage> messages,
        Double temperature,
        Integer maxTokens
) {
}
