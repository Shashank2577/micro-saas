package com.microsaas.customersuccessos.service;

import com.microsaas.customersuccessos.model.*;
import com.microsaas.customersuccessos.repository.*;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerSuccessServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private HealthScoreRepository healthScoreRepository;

    @Mock
    private ExpansionOpportunityRepository expansionOpportunityRepository;

    @InjectMocks
    private CustomerSuccessService service;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testCreateAccount() {
        Account acc = new Account();
        acc.setName("Test Account");

        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);

        Account created = service.createAccount(acc);
        assertNotNull(created.getId());
        assertEquals(tenantId, created.getTenantId());
        assertNotNull(created.getCreatedAt());
    }

    @Test
    void testGetAccount() {
        UUID accId = UUID.randomUUID();
        Account acc = new Account();
        acc.setId(accId);
        acc.setTenantId(tenantId);
        acc.setName("Test");

        when(accountRepository.findById(accId)).thenReturn(Optional.of(acc));

        Account found = service.getAccount(accId);
        assertEquals(accId, found.getId());
    }

    @Test
    void testRecordHealthScore() {
        UUID accId = UUID.randomUUID();
        HealthScore score = new HealthScore();
        score.setScore(85);

        when(healthScoreRepository.save(any(HealthScore.class))).thenAnswer(i -> i.getArguments()[0]);

        HealthScore saved = service.recordHealthScore(accId, score);
        assertNotNull(saved.getId());
        assertEquals(tenantId, saved.getTenantId());
        assertEquals(accId, saved.getAccountId());
    }
    
    @Test
    void testGetLatestHealthScore() {
        UUID accId = UUID.randomUUID();
        HealthScore score = new HealthScore();
        score.setScore(85);
        when(healthScoreRepository.findByTenantIdAndAccountIdOrderByRecordedAtDesc(tenantId, accId))
            .thenReturn(Collections.singletonList(score));
            
        HealthScore latest = service.getLatestHealthScore(accId);
        assertNotNull(latest);
        assertEquals(85, latest.getScore());
    }
    
    @Test
    void testCreateExpansionOpportunity() {
        UUID accId = UUID.randomUUID();
        ExpansionOpportunity opp = new ExpansionOpportunity();
        opp.setType("UPSELL");
        
        when(expansionOpportunityRepository.save(any(ExpansionOpportunity.class))).thenAnswer(i -> i.getArguments()[0]);
        
        ExpansionOpportunity saved = service.createExpansionOpportunity(accId, opp);
        assertNotNull(saved.getId());
        assertEquals(tenantId, saved.getTenantId());
        assertEquals(accId, saved.getAccountId());
        assertEquals("IDENTIFIED", saved.getStatus());
    }
}
