package com.microsaas.integrationmesh.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.integrationmesh.dto.FieldMappingDto;
import com.microsaas.integrationmesh.model.Connector;
import com.microsaas.integrationmesh.model.FieldMapping;
import com.microsaas.integrationmesh.model.Integration;
import com.microsaas.integrationmesh.repository.FieldMappingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MappingServiceTest {

    @Mock
    private FieldMappingRepository mappingRepository;

    @Mock
    private IntegrationService integrationService;

    @Mock
    private AiService aiService;

    @InjectMocks
    private MappingService mappingService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testSaveMapping() {
        UUID integrationId = UUID.randomUUID();
        Integration integration = new Integration();
        
        when(integrationService.getIntegration(integrationId)).thenReturn(integration);
        when(mappingRepository.save(any(FieldMapping.class))).thenAnswer(i -> {
            FieldMapping saved = i.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        FieldMappingDto dto = new FieldMappingDto();
        dto.setSourceField("Name");
        dto.setTargetField("full_name");

        FieldMapping mapping = mappingService.saveMapping(integrationId, dto);

        assertEquals("Name", mapping.getSourceField());
        assertEquals("full_name", mapping.getTargetField());
    }

    @Test
    void testSuggestMappings() {
        UUID integrationId = UUID.randomUUID();
        Integration integration = new Integration();
        Connector source = new Connector(); source.setType("source_type");
        Connector target = new Connector(); target.setType("target_type");
        integration.setSourceConnector(source);
        integration.setTargetConnector(target);

        when(integrationService.getIntegration(integrationId)).thenReturn(integration);
        when(aiService.chat(any(ChatRequest.class))).thenReturn(new ChatResponse("1", "model", "{\"sourceField\": \"source\", \"targetField\": \"target\"}", null));
        when(mappingRepository.save(any(FieldMapping.class))).thenAnswer(i -> i.getArgument(0));

        List<FieldMapping> mappings = mappingService.suggestMappings(integrationId);

        assertEquals(1, mappings.size());
        assertTrue(mappings.get(0).getIsAiSuggested());
        assertEquals("suggested_source", mappings.get(0).getSourceField());
    }
}
