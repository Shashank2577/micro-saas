package com.crosscutting.starter.audit;

import com.crosscutting.starter.CcProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SystemAuditFilterTest {

    @Mock
    private SystemAuditLogRepository repository;

    private SystemAuditFilter filter;
    private CcProperties properties;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        properties = new CcProperties();
        properties.getAudit().setSystemAuditEnabled(true);
        properties.getAudit().setLogRequestBody(true);
        properties.getAudit().setExcludePaths(List.of("/actuator/**", "/cc/health"));
        properties.getAudit().setSensitiveFields(List.of("password", "secret", "token"));

        objectMapper = new ObjectMapper();
        RequestBodySanitizer sanitizer = new RequestBodySanitizer(properties, objectMapper);
        filter = new SystemAuditFilter(repository, sanitizer, properties, objectMapper);

        lenient().when(repository.save(any(SystemAuditLog.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void recordsAuditLogForApiRequest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/cc/tenants");
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(200);

        FilterChain chain = (req, res) -> {
            // Simulate response
            ((HttpServletResponse) res).setStatus(200);
        };

        filter.doFilter(request, response, chain);

        ArgumentCaptor<SystemAuditLog> captor = ArgumentCaptor.forClass(SystemAuditLog.class);
        verify(repository).save(captor.capture());

        SystemAuditLog log = captor.getValue();
        assertEquals("GET", log.getRequestMethod());
        assertEquals("/cc/tenants", log.getRequestPath());
        assertEquals("API", log.getEventType());
        assertEquals(200, log.getResponseStatus());
        assertNotNull(log.getDurationMs());
    }

    @Test
    void classifiesAuthEventType() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/cc/auth/login");
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (req, res) -> {};

        filter.doFilter(request, response, chain);

        ArgumentCaptor<SystemAuditLog> captor = ArgumentCaptor.forClass(SystemAuditLog.class);
        verify(repository).save(captor.capture());

        assertEquals("AUTH", captor.getValue().getEventType());
    }

    @Test
    void classifiesRbacEventType() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/cc/rbac/roles");
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (req, res) -> {};

        filter.doFilter(request, response, chain);

        ArgumentCaptor<SystemAuditLog> captor = ArgumentCaptor.forClass(SystemAuditLog.class);
        verify(repository).save(captor.capture());

        assertEquals("RBAC", captor.getValue().getEventType());
    }

    @Test
    void skipsExcludedPaths() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/cc/health");
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (req, res) -> {};

        filter.doFilter(request, response, chain);

        verify(repository, never()).save(any());
    }

    @Test
    void skipsActuatorPaths() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/actuator/health");
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (req, res) -> {};

        filter.doFilter(request, response, chain);

        verify(repository, never()).save(any());
    }

    @Test
    void capturesCorrelationIdFromMdc() throws ServletException, IOException {
        UUID correlationId = UUID.randomUUID();
        MDC.put("correlationId", correlationId.toString());

        try {
            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/cc/api/test");
            MockHttpServletResponse response = new MockHttpServletResponse();

            FilterChain chain = (req, res) -> {};

            filter.doFilter(request, response, chain);

            ArgumentCaptor<SystemAuditLog> captor = ArgumentCaptor.forClass(SystemAuditLog.class);
            verify(repository).save(captor.capture());

            assertEquals(correlationId, captor.getValue().getCorrelationId());
        } finally {
            MDC.clear();
        }
    }

    @Test
    void capturesClientIpAddress() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/cc/api/test");
        request.setRemoteAddr("192.168.1.100");
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (req, res) -> {};

        filter.doFilter(request, response, chain);

        ArgumentCaptor<SystemAuditLog> captor = ArgumentCaptor.forClass(SystemAuditLog.class);
        verify(repository).save(captor.capture());

        assertEquals("192.168.1.100", captor.getValue().getIpAddress());
    }

    @Test
    void capturesXForwardedForIp() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/cc/api/test");
        request.addHeader("X-Forwarded-For", "10.0.0.1, 192.168.1.1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (req, res) -> {};

        filter.doFilter(request, response, chain);

        ArgumentCaptor<SystemAuditLog> captor = ArgumentCaptor.forClass(SystemAuditLog.class);
        verify(repository).save(captor.capture());

        assertEquals("10.0.0.1", captor.getValue().getIpAddress());
    }

    @Test
    void capturesUserAgent() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/cc/api/test");
        request.addHeader("User-Agent", "Mozilla/5.0 TestBrowser");
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (req, res) -> {};

        filter.doFilter(request, response, chain);

        ArgumentCaptor<SystemAuditLog> captor = ArgumentCaptor.forClass(SystemAuditLog.class);
        verify(repository).save(captor.capture());

        assertEquals("Mozilla/5.0 TestBrowser", captor.getValue().getUserAgent());
    }

    @Test
    void continuesFilterChainEvenWhenAuditFails() throws ServletException, IOException {
        when(repository.save(any())).thenThrow(new RuntimeException("DB error"));

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/cc/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (req, res) -> {
            ((HttpServletResponse) res).setStatus(200);
        };

        // Should not throw
        assertDoesNotThrow(() -> filter.doFilter(request, response, chain));
    }
}
