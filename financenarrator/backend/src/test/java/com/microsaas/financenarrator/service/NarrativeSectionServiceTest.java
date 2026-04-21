package com.microsaas.financenarrator.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.financenarrator.model.NarrativeSection;
import com.microsaas.financenarrator.repository.NarrativeSectionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NarrativeSectionServiceTest {

    @Mock
    private NarrativeSectionRepository repository;

    @Mock
    private EventPublisherService eventPublisher;

    @InjectMocks
    private NarrativeSectionService service;

    private MockedStatic<TenantContext> tenantContextMockedStatic;
    private UUID tenantId;
    private UUID sectionId;
    private NarrativeSection section;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        sectionId = UUID.randomUUID();
        tenantContextMockedStatic = Mockito.mockStatic(TenantContext.class);
        tenantContextMockedStatic.when(TenantContext::require).thenReturn(tenantId);

        section = new NarrativeSection();
        section.setId(sectionId);
        section.setTenantId(tenantId);
        section.setName("Introduction");
        section.setStatus("DRAFT");
    }

    @AfterEach
    void tearDown() {
        tenantContextMockedStatic.close();
    }

    @Test
    void testAddSection_toRequest() {
        when(repository.save(any(NarrativeSection.class))).thenReturn(section);

        NarrativeSection result = service.create(section);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        verify(repository, times(1)).save(section);
        verify(eventPublisher, times(1)).publishEvent(
                eq("financenarrator.narrative.generated"),
                eq(tenantId),
                eq("financenarrator"),
                any(Map.class)
        );
    }

    @Test
    void testListSections_byRequestId() {
        when(repository.findByTenantId(tenantId)).thenReturn(List.of(section));

        List<NarrativeSection> result = service.list();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(repository, times(1)).findByTenantId(tenantId);
    }

    @Test
    void testUpdateSection_content() {
        NarrativeSection updateDto = new NarrativeSection();
        updateDto.setName("Updated Introduction");
        updateDto.setStatus("VALIDATED");
        updateDto.setMetadataJson("{\"key\":\"value\"}");

        when(repository.findByIdAndTenantId(sectionId, tenantId)).thenReturn(Optional.of(section));
        when(repository.save(any(NarrativeSection.class))).thenReturn(section);

        NarrativeSection result = service.update(sectionId, updateDto);

        assertNotNull(result);
        assertEquals("Updated Introduction", result.getName());
        assertEquals("VALIDATED", result.getStatus());
        assertEquals("{\"key\":\"value\"}", result.getMetadataJson());
        verify(repository, times(1)).findByIdAndTenantId(sectionId, tenantId);
        verify(repository, times(1)).save(section);
    }

    @Test
    void testDeleteSection() {
        when(repository.findByIdAndTenantId(sectionId, tenantId)).thenReturn(Optional.of(section));
        doNothing().when(repository).delete(section);

        service.delete(sectionId);

        verify(repository, times(1)).findByIdAndTenantId(sectionId, tenantId);
        verify(repository, times(1)).delete(section);
    }
}
