package com.microsaas.competitorradar.service;

import com.microsaas.competitorradar.dto.CompetitorDto;
import com.microsaas.competitorradar.model.Competitor;
import com.microsaas.competitorradar.repository.CompetitorRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitorServiceTest {

    @Mock
    private CompetitorRepository competitorRepository;

    @InjectMocks
    private CompetitorService competitorService;

    private final UUID tenantId = UUID.randomUUID();
    private final UUID competitorId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetCompetitors() {
        Competitor c = Competitor.builder().id(competitorId).tenantId(tenantId).name("Test Corp").build();
        when(competitorRepository.findByTenantId(tenantId)).thenReturn(List.of(c));

        List<CompetitorDto> result = competitorService.getCompetitors();

        assertEquals(1, result.size());
        assertEquals("Test Corp", result.get(0).getName());
    }

    @Test
    void testAddCompetitor() {
        CompetitorDto dto = new CompetitorDto();
        dto.setName("New Corp");

        Competitor saved = Competitor.builder().id(competitorId).tenantId(tenantId).name("New Corp").build();
        when(competitorRepository.save(any(Competitor.class))).thenReturn(saved);

        CompetitorDto result = competitorService.addCompetitor(dto);

        assertNotNull(result);
        assertEquals("New Corp", result.getName());
    }

    @Test
    void testRemoveCompetitor() {
        Competitor c = Competitor.builder().id(competitorId).tenantId(tenantId).build();
        when(competitorRepository.findByIdAndTenantId(competitorId, tenantId)).thenReturn(Optional.of(c));

        competitorService.removeCompetitor(competitorId);

        verify(competitorRepository, times(1)).delete(c);
    }
}
