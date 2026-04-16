package com.microsaas.ndaflow.service;

import com.microsaas.ndaflow.domain.Nda;
import com.microsaas.ndaflow.domain.NdaClause;
import com.microsaas.ndaflow.domain.NdaStatus;
import com.microsaas.ndaflow.domain.NdaType;
import com.microsaas.ndaflow.repository.NdaClauseRepository;
import com.microsaas.ndaflow.repository.NdaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class NdaServiceTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.url", postgres::getJdbcUrl);
        registry.add("spring.flyway.user", postgres::getUsername);
        registry.add("spring.flyway.password", postgres::getPassword);
    }

    @Autowired
    private NdaService ndaService;

    @Autowired
    private NdaRepository ndaRepository;

    @Autowired
    private NdaClauseRepository ndaClauseRepository;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        ndaRepository.deleteAll();
        ndaClauseRepository.deleteAll();
        tenantId = UUID.randomUUID();
    }

    @Test
    void generateNdaCreatesNdaWithStandardClauses() {
        Nda nda = ndaService.generateNda("Acme Mutual NDA", "Acme Corp", NdaType.MUTUAL, tenantId);

        assertThat(nda.getId()).isNotNull();
        assertThat(nda.getStatus()).isEqualTo(NdaStatus.DRAFT);
        assertThat(nda.getTitle()).isEqualTo("Acme Mutual NDA");

        List<NdaClause> clauses = ndaClauseRepository.findByNdaIdAndTenantId(nda.getId(), tenantId);
        assertThat(clauses).hasSize(5);
    }

    @Test
    void sendNdaChangesStatusToSent() {
        Nda nda = ndaService.generateNda("Test NDA", "Test Corp", NdaType.ONE_WAY, tenantId);
        
        Nda sentNda = ndaService.sendNda(nda.getId(), tenantId);
        
        assertThat(sentNda.getStatus()).isEqualTo(NdaStatus.SENT);
    }

    @Test
    void listExpiringSoonReturnsOnlyNdasWithinWindow() {
        Nda nda1 = ndaService.generateNda("Expiring NDA", "Corp A", NdaType.MUTUAL, tenantId);
        nda1.setExpiresAt(LocalDate.now().plusDays(10));
        ndaRepository.save(nda1);

        Nda nda2 = ndaService.generateNda("Not Expiring NDA", "Corp B", NdaType.ONE_WAY, tenantId);
        nda2.setExpiresAt(LocalDate.now().plusDays(40));
        ndaRepository.save(nda2);

        List<Nda> expiringNdas = ndaService.listExpiringSoon(tenantId, 30);

        assertThat(expiringNdas).hasSize(1);
        assertThat(expiringNdas.get(0).getId()).isEqualTo(nda1.getId());
    }
}
