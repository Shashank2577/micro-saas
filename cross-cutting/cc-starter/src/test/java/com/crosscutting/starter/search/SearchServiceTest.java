package com.crosscutting.starter.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private SearchService service;

    @BeforeEach
    void setUp() {
        service = new SearchService(jdbcTemplate);
    }

    @Test
    void indexUpsertsDocument() {
        UUID tenantId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();

        service.index(tenantId, "Article", resourceId, "Hello world", "{\"tags\":[\"test\"]}");

        ArgumentCaptor<Object[]> paramsCaptor = ArgumentCaptor.forClass(Object[].class);
        verify(jdbcTemplate).update(contains("INSERT INTO cc.search_index"), paramsCaptor.capture());

        Object[] params = paramsCaptor.getValue();
        assertEquals(tenantId, params[0]);
        assertEquals("Article", params[1]);
        assertEquals(resourceId, params[2]);
        assertEquals("Hello world", params[3]);
        assertEquals("{\"tags\":[\"test\"]}", params[4]);
    }

    @Test
    void searchQueriesWithResourceType() {
        UUID tenantId = UUID.randomUUID();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenReturn(List.of());

        List<SearchResult> results = service.search(tenantId, "hello", "Article", 10);

        assertNotNull(results);
        verify(jdbcTemplate).query(
                contains("resource_type = ?"),
                any(RowMapper.class),
                eq("hello"), eq(tenantId), eq("Article"), eq("hello"), eq(10)
        );
    }

    @Test
    void searchQueriesWithoutResourceType() {
        UUID tenantId = UUID.randomUUID();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenReturn(List.of());

        List<SearchResult> results = service.search(tenantId, "hello", null, 20);

        assertNotNull(results);
        verify(jdbcTemplate).query(
                argThat((String sql) -> !sql.contains("resource_type = ?")),
                any(RowMapper.class),
                eq("hello"), eq(tenantId), eq("hello"), eq(20)
        );
    }

    @Test
    void deleteIndexRemovesDocument() {
        UUID tenantId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();

        service.deleteIndex(tenantId, "Article", resourceId);

        verify(jdbcTemplate).update(
                contains("DELETE FROM cc.search_index"),
                eq(tenantId), eq("Article"), eq(resourceId)
        );
    }

    @Test
    void semanticSearchQueriesEmbeddingsTable() {
        UUID tenantId = UUID.randomUUID();
        double[] embedding = {0.1, 0.2, 0.3};
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenReturn(List.of());

        List<SearchResult> results = service.semanticSearch(tenantId, embedding, null, 10);

        assertNotNull(results);
        verify(jdbcTemplate).query(
                contains("cc.embeddings"),
                any(RowMapper.class),
                eq("[0.1,0.2,0.3]"), eq(tenantId), eq("[0.1,0.2,0.3]"), eq(10)
        );
    }

    @Test
    void hybridSearchCombinesTextAndVector() {
        UUID tenantId = UUID.randomUUID();
        double[] embedding = {0.5, 0.6};
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenReturn(List.of());

        List<SearchResult> results = service.hybridSearch(
                tenantId, "test query", embedding, 0.7, 0.3, "Article", 15);

        assertNotNull(results);
        verify(jdbcTemplate).query(
                argThat((String sql) -> sql.contains("ts_rank") && sql.contains("embedding")),
                any(RowMapper.class),
                eq(0.7), eq("test query"), eq(0.3), eq("[0.5,0.6]"),
                eq(tenantId), eq("Article"), eq("test query"), eq(15)
        );
    }
}
