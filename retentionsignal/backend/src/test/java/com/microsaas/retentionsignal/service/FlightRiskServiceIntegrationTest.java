package com.microsaas.retentionsignal.service;

import com.microsaas.retentionsignal.RetentionSignalApplication;
import com.microsaas.retentionsignal.model.FlightRiskScore;
import com.microsaas.retentionsignal.model.RetentionSignal;
import com.microsaas.retentionsignal.model.SignalType;
import com.microsaas.retentionsignal.repository.FlightRiskScoreRepository;
import com.microsaas.retentionsignal.repository.RetentionSignalRepository;
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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = RetentionSignalApplication.class)
@ActiveProfiles("test")
@Testcontainers
public class FlightRiskServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private FlightRiskService flightRiskService;

    @Autowired
    private RetentionSignalRepository retentionSignalRepository;

    @Autowired
    private FlightRiskScoreRepository flightRiskScoreRepository;

    private UUID tenantId;
    private UUID employeeId;

    @BeforeEach
    void setUp() {
        retentionSignalRepository.deleteAll();
        flightRiskScoreRepository.deleteAll();
        tenantId = UUID.randomUUID();
        employeeId = UUID.randomUUID();
    }

    @Test
    void testCalculateRisk_sumWeights() {
        // Given
        createSignal(SignalType.COMP_GAP, 1.0); // 30
        createSignal(SignalType.MANAGER_ISSUES, 1.0); // 15

        // When
        FlightRiskScore score = flightRiskService.calculateRisk(employeeId, tenantId);

        // Then
        assertThat(score).isNotNull();
        assertThat(score.getScore()).isEqualTo(45);
        assertThat(score.getTopRiskFactors()).contains("COMP_GAP", "MANAGER_ISSUES");
        assertThat(score.getEmployeeId()).isEqualTo(employeeId);
        assertThat(score.getTenantId()).isEqualTo(tenantId);
    }

    @Test
    void testCalculateRisk_cappedAt100() {
        // Given
        createSignal(SignalType.COMP_GAP, 1.0); // 30
        createSignal(SignalType.LOW_ENGAGEMENT, 1.0); // 25
        createSignal(SignalType.SLOW_PROGRESSION, 1.0); // 20
        createSignal(SignalType.MANAGER_ISSUES, 1.0); // 15
        createSignal(SignalType.PEER_DEPARTURES, 1.0); // 10
        createSignal(SignalType.COMP_GAP, 1.0); // 30 (adding duplicate type just to exceed 100 for test)

        // When
        FlightRiskScore score = flightRiskService.calculateRisk(employeeId, tenantId);

        // Then
        assertThat(score.getScore()).isEqualTo(100);
    }

    @Test
    void testGetHighRisk() {
        // Given
        // Emloyee 1: score 70 (high risk)
        UUID emp1 = UUID.randomUUID();
        createSignalForEmployee(emp1, SignalType.COMP_GAP, 1.0); // 30
        createSignalForEmployee(emp1, SignalType.LOW_ENGAGEMENT, 1.0); // 25
        createSignalForEmployee(emp1, SignalType.MANAGER_ISSUES, 1.0); // 15
        flightRiskService.calculateRisk(emp1, tenantId);

        // Employee 2: score 65 (not high risk)
        UUID emp2 = UUID.randomUUID();
        createSignalForEmployee(emp2, SignalType.COMP_GAP, 1.0); // 30
        createSignalForEmployee(emp2, SignalType.LOW_ENGAGEMENT, 1.0); // 25
        createSignalForEmployee(emp2, SignalType.PEER_DEPARTURES, 1.0); // 10
        flightRiskService.calculateRisk(emp2, tenantId);

        // Employee 3: score 80 (high risk)
        UUID emp3 = UUID.randomUUID();
        createSignalForEmployee(emp3, SignalType.COMP_GAP, 1.0); // 30
        createSignalForEmployee(emp3, SignalType.LOW_ENGAGEMENT, 1.0); // 25
        createSignalForEmployee(emp3, SignalType.SLOW_PROGRESSION, 1.0); // 20
        flightRiskService.calculateRisk(emp3, tenantId);

        // When
        List<FlightRiskScore> highRiskScores = flightRiskService.getHighRisk(tenantId);

        // Then
        assertThat(highRiskScores).hasSize(2);
        assertThat(highRiskScores).extracting("employeeId").containsExactlyInAnyOrder(emp1, emp3);
    }

    private void createSignal(SignalType type, double value) {
        createSignalForEmployee(employeeId, type, value);
    }

    private void createSignalForEmployee(UUID empId, SignalType type, double value) {
        RetentionSignal signal = new RetentionSignal();
        signal.setEmployeeId(empId);
        signal.setSignalType(type);
        signal.setValue(value);
        signal.setTenantId(tenantId);
        retentionSignalRepository.save(signal);
    }
}
