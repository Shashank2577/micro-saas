package com.microsaas.datastoryteller.api;

import com.microsaas.datastoryteller.domain.model.Dataset;
import com.microsaas.datastoryteller.domain.model.DataSource;
import com.microsaas.datastoryteller.domain.model.DataSourceType;
import com.microsaas.datastoryteller.domain.model.DataSourceStatus;
import com.microsaas.datastoryteller.domain.model.Insight;
import com.microsaas.datastoryteller.repository.DataSourceRepository;
import com.microsaas.datastoryteller.repository.DatasetRepository;
import com.microsaas.datastoryteller.service.AiService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
public class AttributionControllerTest {

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
    private DataSourceRepository dataSourceRepository;

    @MockBean
    private AiService aiService;

    private String tenantId = "tenant-test";
    private Dataset dataset;

    @BeforeEach
    void setUp() {
        datasetRepository.deleteAll();
        dataSourceRepository.deleteAll();

        DataSource ds = DataSource.builder()
                .tenantId(tenantId)
                .name("Test DS")
                .type(DataSourceType.POSTGRES)
                .status(DataSourceStatus.ACTIVE)
                .build();
        ds = dataSourceRepository.save(ds);

        dataset = Dataset.builder()
                .tenantId(tenantId)
                .dataSource(ds)
                .name("Sample Data")
                .sqlQuery("SELECT * FROM sales")
                .schemaJson("{}")
                .build();
        dataset = datasetRepository.save(dataset);
    }

    @Test
    void testAttributeDelta() {
        // Setup AI mock for AC 12: Attribution decomposes a 20% delta into 3 segment drivers
        String aiResponse = "The 20% revenue drop is driven by:\n1. EU Region (-12%)\n2. Widget B (-5%)\n3. Mobile Channel (-3%)";
        when(aiService.generateNarrative(eq("attribute-metric"), eq(tenantId), anyString(), anyString()))
                .thenReturn(aiResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Tenant-ID", tenantId);

        AttributionRequest req = new AttributionRequest();
        req.setDatasetId(dataset.getId());
        req.setMetric("revenue");
        req.setDeltaPercent(-20.0);

        HttpEntity<AttributionRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<Insight> response = restTemplate.exchange("/attribution", HttpMethod.POST, entity, Insight.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getDescription()).contains("EU Region", "Widget B", "Mobile Channel");
    }
}
