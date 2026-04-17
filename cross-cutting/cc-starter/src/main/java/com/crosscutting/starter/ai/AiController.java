package com.crosscutting.starter.ai;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cc/ai")
@Tag(name = "AI", description = "AI chat completions, embeddings, and model management")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    @Operation(summary = "Send a chat request", description = "Send a chat completion request to the configured AI provider")
    @ApiResponse(responseCode = "200", description = "Chat response returned successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "AI provider error")
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        return aiService.chat(request);
    }

    @PostMapping("/embed")
    @Operation(summary = "Generate an embedding", description = "Generate a vector embedding for the given text using the specified model")
    @ApiResponse(responseCode = "200", description = "Embedding generated successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "AI provider error")
    public Map<String, Object> embed(@Valid @RequestBody EmbedRequest request) {
        List<Double> embedding = aiService.embed(request.model(), request.text());
        return Map.of("embedding", embedding);
    }

    @GetMapping("/models")
    @Operation(summary = "List available models", description = "Return the list of AI models available for chat and embedding operations")
    @ApiResponse(responseCode = "200", description = "Models listed successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public Map<String, Object> listModels() {
        List<String> models = aiService.listModels();
        return Map.of("models", models);
    }

    public record EmbedRequest(
            @NotBlank(message = "Model is required") String model,
            @NotBlank(message = "Text is required") String text) {
    }
}
