package com.crosscutting.starter.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Mock
    private RestClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private RestClient.ResponseSpec getResponseSpec;

    private AiService aiService;

    @BeforeEach
    void setUp() {
        aiService = new AiService(restClient);
    }

    @Test
    void chat_returnsParsedChatResponse() {
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/chat/completions")).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Object.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);

        Map<String, Object> gatewayResponse = Map.of(
                "id", "chatcmpl-123",
                "model", "gpt-4",
                "choices", List.of(Map.of(
                        "message", Map.of("role", "assistant", "content", "Hello!")
                )),
                "usage", Map.of(
                        "prompt_tokens", 10,
                        "completion_tokens", 5,
                        "total_tokens", 15
                )
        );
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(gatewayResponse);

        ChatRequest request = new ChatRequest(
                "gpt-4",
                List.of(new ChatMessage("user", "Hi")),
                0.7,
                100
        );

        ChatResponse result = aiService.chat(request);

        assertThat(result.id()).isEqualTo("chatcmpl-123");
        assertThat(result.model()).isEqualTo("gpt-4");
        assertThat(result.content()).isEqualTo("Hello!");
        assertThat(result.usage().promptTokens()).isEqualTo(10);
        assertThat(result.usage().completionTokens()).isEqualTo(5);
        assertThat(result.usage().totalTokens()).isEqualTo(15);
    }

    @Test
    void embed_returnsEmbeddingVector() {
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/embeddings")).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Object.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);

        Map<String, Object> gatewayResponse = Map.of(
                "data", List.of(Map.of(
                        "embedding", List.of(0.1, 0.2, 0.3, 0.4)
                ))
        );
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(gatewayResponse);

        List<Double> result = aiService.embed("text-embedding-ada-002", "Hello world");

        assertThat(result).containsExactly(0.1, 0.2, 0.3, 0.4);
    }

    @SuppressWarnings("unchecked")
    @Test
    void listModels_returnsModelIds() {
        doReturn(requestHeadersUriSpec).when(restClient).get();
        doReturn(requestHeadersUriSpec).when(requestHeadersUriSpec).uri("/models");
        doReturn(getResponseSpec).when(requestHeadersUriSpec).retrieve();

        Map<String, Object> gatewayResponse = Map.of(
                "data", List.of(
                        Map.of("id", "gpt-4"),
                        Map.of("id", "gpt-3.5-turbo")
                )
        );
        when(getResponseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(gatewayResponse);

        List<String> result = aiService.listModels();

        assertThat(result).containsExactly("gpt-4", "gpt-3.5-turbo");
    }
}
