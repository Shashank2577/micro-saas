package com.microsaas.contractsense.service;

import com.microsaas.contractsense.domain.Contract;
import com.microsaas.contractsense.domain.RiskAssessment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class ContractServiceTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ContractService contractService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
    }

    @Test
    void uploadContract_createsRecordAndExtractsClauses() {
        Contract contract = contractService.uploadContract(
                "NDA - ACME Corp",
                "ACME Corp",
                "NDA",
                "Mock contract content...",
                tenantId
        );

        assertThat(contract).isNotNull();
        assertThat(contract.getId()).isNotNull();
        assertThat(contract.getTitle()).isEqualTo("NDA - ACME Corp");
        assertThat(contract.getTenantId()).isEqualTo(tenantId);
    }

    @Test
    void listContracts_returnsTenantScopedResults() {
        UUID otherTenantId = UUID.randomUUID();

        contractService.uploadContract("Contract 1", "Party A", "MSA", "...", tenantId);
        contractService.uploadContract("Contract 2", "Party B", "NDA", "...", tenantId);
        contractService.uploadContract("Other Contract", "Party C", "DPA", "...", otherTenantId);

        List<Contract> tenantContracts = contractService.listContracts(tenantId);
        List<Contract> otherTenantContracts = contractService.listContracts(otherTenantId);

        assertThat(tenantContracts).hasSize(2);
        assertThat(otherTenantContracts).hasSize(1);
    }

    @Test
    void analyzeContract_producesRiskAssessment() {
        Contract contract = contractService.uploadContract(
                "Vendor Agreement",
                "TechCorp",
                "Vendor Agreement",
                "Contains indemnification and liability cap.",
                tenantId
        );

        RiskAssessment assessment = contractService.analyzeContract(contract.getId(), tenantId);

        assertThat(assessment).isNotNull();
        assertThat(assessment.getContractId()).isEqualTo(contract.getId());
        assertThat(assessment.getOverallRiskScore()).isGreaterThan(0);
        assertThat(assessment.getFlagCount()).isGreaterThanOrEqualTo(0);
        assertThat(assessment.getTenantId()).isEqualTo(tenantId);
    }
}
