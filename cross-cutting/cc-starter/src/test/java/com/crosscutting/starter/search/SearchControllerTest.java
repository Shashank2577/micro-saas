package com.crosscutting.starter.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
@AutoConfigureMockMvc(addFilters = false)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SearchService searchService;

    @Test
    void indexDocumentReturnsOk() throws Exception {
        UUID tenantId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();

        var request = new SearchController.IndexRequest(
                tenantId, "Article", resourceId, "Test content", "{}"
        );

        mockMvc.perform(post("/cc/search/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(searchService).index(tenantId, "Article", resourceId, "Test content", "{}");
    }

    @Test
    void searchReturnsResults() throws Exception {
        UUID tenantId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();
        SearchResult result = new SearchResult("Article", resourceId, "Test content", "{}", 0.85);
        when(searchService.search(eq(tenantId), eq("hello"), isNull(), eq(20)))
                .thenReturn(List.of(result));

        mockMvc.perform(get("/cc/search")
                        .param("tenantId", tenantId.toString())
                        .param("q", "hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].resourceType").value("Article"))
                .andExpect(jsonPath("$[0].resourceId").value(resourceId.toString()))
                .andExpect(jsonPath("$[0].score").value(0.85));
    }

    @Test
    void searchWithTypeFilters() throws Exception {
        UUID tenantId = UUID.randomUUID();
        when(searchService.search(eq(tenantId), eq("hello"), eq("Article"), eq(10)))
                .thenReturn(List.of());

        mockMvc.perform(get("/cc/search")
                        .param("tenantId", tenantId.toString())
                        .param("q", "hello")
                        .param("type", "Article")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void semanticSearchReturnsResults() throws Exception {
        UUID tenantId = UUID.randomUUID();
        var request = new SearchController.SemanticSearchRequest(
                tenantId, new double[]{0.1, 0.2, 0.3}, "Article", 10
        );
        when(searchService.semanticSearch(eq(tenantId), any(double[].class), eq("Article"), eq(10)))
                .thenReturn(List.of());

        mockMvc.perform(post("/cc/search/semantic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void hybridSearchReturnsResults() throws Exception {
        UUID tenantId = UUID.randomUUID();
        var request = new SearchController.HybridSearchRequest(
                tenantId, "test query", new double[]{0.1, 0.2}, 0.7, 0.3, "Article", 15
        );
        when(searchService.hybridSearch(eq(tenantId), eq("test query"), any(double[].class),
                eq(0.7), eq(0.3), eq("Article"), eq(15)))
                .thenReturn(List.of());

        mockMvc.perform(post("/cc/search/hybrid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deleteIndexReturnsOk() throws Exception {
        UUID tenantId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();

        var request = new SearchController.DeleteIndexRequest(tenantId, "Article", resourceId);

        mockMvc.perform(delete("/cc/search/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(searchService).deleteIndex(tenantId, "Article", resourceId);
    }
}
