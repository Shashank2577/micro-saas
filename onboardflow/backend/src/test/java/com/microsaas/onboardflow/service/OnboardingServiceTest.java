package com.microsaas.onboardflow.service;

import com.crosscutting.ai.AiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.onboardflow.model.OnboardingPlan;
import com.microsaas.onboardflow.repository.OnboardingMilestoneRepository;
import com.microsaas.onboardflow.repository.OnboardingPlanRepository;
import com.microsaas.onboardflow.repository.OnboardingTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
class OnboardingServiceTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "false");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private OnboardingService onboardingService;

    @Autowired
    private OnboardingPlanRepository planRepository;

    @MockBean
    private AiService aiService;

    @BeforeEach
    void setUp() {
        planRepository.deleteAll();
    }

    @Test
    void shouldGeneratePlan() {
        String mockAiResponse = "{\"plan30Day\": {\"milestone\": \"Learn basics\"}, \"plan60Day\": {\"milestone\": \"Take a project\"}, \"plan90Day\": {\"milestone\": \"Lead a project\"}}";
        when(aiService.generateText(anyString())).thenReturn(mockAiResponse);

        UUID empId = UUID.randomUUID();
        OnboardingPlan plan = onboardingService.generatePlan(empId, "Software Engineer", "Engineering", LocalDate.now());

        assertThat(plan.getId()).isNotNull();
        assertThat(plan.getEmployeeId()).isEqualTo(empId);
        assertThat(plan.getPlan30Day()).containsKey("milestone");
        assertThat(plan.getStatus()).isEqualTo(OnboardingPlan.Status.DRAFT);
    }
}
