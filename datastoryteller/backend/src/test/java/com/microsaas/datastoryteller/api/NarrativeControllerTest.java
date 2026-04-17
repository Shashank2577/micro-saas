package com.microsaas.datastoryteller.api;

import com.microsaas.datastoryteller.domain.model.Dataset;
import com.microsaas.datastoryteller.domain.model.NarrativeReport;
import com.microsaas.datastoryteller.domain.model.NarrativeTemplate;
import com.microsaas.datastoryteller.domain.model.ReportStatus;
import com.microsaas.datastoryteller.repository.DatasetRepository;
import com.microsaas.datastoryteller.repository.NarrativeReportRepository;
import com.microsaas.datastoryteller.repository.NarrativeTemplateRepository;
import com.microsaas.datastoryteller.service.AiService;
import com.microsaas.datastoryteller.event.EventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
public class NarrativeControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DatasetRepository datasetRepository;

    @Autowired
    private NarrativeTemplateRepository templateRepository;

    @Autowired
    private NarrativeReportRepository reportRepository;

    @MockBean
    private AiService aiService;

    @MockBean
    private EventHandler eventHandler;

    private String tenantId = "tenant-test";
    private Dataset dataset;
    private NarrativeTemplate template;

    @BeforeEach
    void setUp() {
        reportRepository.deleteAll();
        datasetRepository.deleteAll();
        templateRepository.deleteAll();

        // Create dummy dataset (simulating AC: Generate a narrative from a 50-row sample dataset)
        Dataset ds = Dataset.builder()
                .tenantId(tenantId)
                .name("Sample Sales Data")
                .sqlQuery("SELECT * FROM sales")
                .schemaJson("{\"type\": \"object\", \"properties\": {\"revenue\": {}, \"signups\": {}, \"churn\": {}}}")
                .build();
        // bypass foreign key logic in test or create datasource first
        // Note: in full integration test we'd create DataSource first.
    }

    // We will test cross-tenant logic.
    @Test
    void testCrossTenantReadFails() {
        // Setup data for tenant A
        NarrativeTemplate t = NarrativeTemplate.builder().tenantId(tenantId).name("T1").promptTemplate("p").build();
        t = templateRepository.save(t);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Tenant-ID", "tenant-other");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // Try reading templates from tenant B
        ResponseEntity<NarrativeTemplate[]> response = restTemplate.exchange("/templates", HttpMethod.GET, entity, NarrativeTemplate[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }
}
