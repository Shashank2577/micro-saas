package com.crosscutting.starter.ai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AiController.class)
@AutoConfigureMockMvc(addFilters = false)
class AiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AiService aiService;

    @Test
    void chat_returnsChatResponse() throws Exception {
        ChatResponse response = new ChatResponse(
                "chatcmpl-123", "gpt-4", "Hello!",
                new ChatResponse.Usage(10, 5, 15)
        );
        when(aiService.chat(any(ChatRequest.class))).thenReturn(response);

        mockMvc.perform(post("/cc/ai/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"model":"gpt-4","messages":[{"role":"user","content":"Hi"}],"temperature":0.7,"maxTokens":100}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("chatcmpl-123"))
                .andExpect(jsonPath("$.model").value("gpt-4"))
                .andExpect(jsonPath("$.content").value("Hello!"))
                .andExpect(jsonPath("$.usage.promptTokens").value(10));
    }

    @Test
    void embed_returnsEmbeddingVector() throws Exception {
        when(aiService.embed(eq("text-embedding-ada-002"), eq("Hello")))
                .thenReturn(List.of(0.1, 0.2, 0.3));

        mockMvc.perform(post("/cc/ai/embed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"model":"text-embedding-ada-002","text":"Hello"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.embedding[0]").value(0.1))
                .andExpect(jsonPath("$.embedding[1]").value(0.2))
                .andExpect(jsonPath("$.embedding[2]").value(0.3));
    }

    @Test
    void listModels_returnsModelList() throws Exception {
        when(aiService.listModels()).thenReturn(List.of("gpt-4", "gpt-3.5-turbo"));

        mockMvc.perform(get("/cc/ai/models"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.models[0]").value("gpt-4"))
                .andExpect(jsonPath("$.models[1]").value("gpt-3.5-turbo"));
    }
}
