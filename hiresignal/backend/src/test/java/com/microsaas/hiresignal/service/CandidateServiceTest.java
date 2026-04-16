package com.microsaas.hiresignal.service;

import com.crosscutting.event.EventPublisher;
import com.microsaas.hiresignal.model.Candidate;
import com.microsaas.hiresignal.repository.CandidateRepository;
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

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
class CandidateServiceTest {

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
    private CandidateService candidateService;

    @Autowired
    private CandidateRepository candidateRepository;

    @MockBean
    private EventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        candidateRepository.deleteAll();
    }

    @Test
    void shouldAddAndScoreCandidate() {
        UUID reqId = UUID.randomUUID();
        Candidate candidate = candidateService.addCandidate("John Doe", "john@example.com", Candidate.Source.REFERRAL, reqId);
        
        assertThat(candidate.getId()).isNotNull();
        assertThat(candidate.getStatus()).isEqualTo(Candidate.Status.NEW);

        Candidate scored = candidateService.scoreFit(candidate.getId());
        assertThat(scored.getFitScore()).isEqualTo(85);
    }

    @Test
    void shouldAdvanceAndEmitEvent() {
        UUID reqId = UUID.randomUUID();
        Candidate candidate = candidateService.addCandidate("Jane Doe", "jane@example.com", Candidate.Source.LINKEDIN, reqId);
        
        candidate = candidateService.advance(candidate.getId()); // to SCREENING
        assertThat(candidate.getStatus()).isEqualTo(Candidate.Status.SCREENING);
        
        candidate = candidateService.advance(candidate.getId()); // to INTERVIEWING
        candidate = candidateService.advance(candidate.getId()); // to OFFERED
        candidate = candidateService.advance(candidate.getId()); // to HIRED
        
        assertThat(candidate.getStatus()).isEqualTo(Candidate.Status.HIRED);
        verify(eventPublisher).publish(eq("candidate.hired"), eq(Map.of(
                "candidateId", candidate.getId().toString(),
                "requisitionId", reqId.toString()
        )));
    }

    @Test
    void shouldListByReq() {
        UUID reqId1 = UUID.randomUUID();
        UUID reqId2 = UUID.randomUUID();
        
        candidateService.addCandidate("Alice", "alice@example.com", Candidate.Source.DIRECT, reqId1);
        candidateService.addCandidate("Bob", "bob@example.com", Candidate.Source.JOB_BOARD, reqId1);
        candidateService.addCandidate("Charlie", "charlie@example.com", Candidate.Source.REFERRAL, reqId2);
        
        List<Candidate> candidates1 = candidateService.listByReq(reqId1);
        assertThat(candidates1).hasSize(2);
        
        List<Candidate> candidates2 = candidateService.listByReq(reqId2);
        assertThat(candidates2).hasSize(1);
        assertThat(candidates2.get(0).getName()).isEqualTo("Charlie");
    }
}
