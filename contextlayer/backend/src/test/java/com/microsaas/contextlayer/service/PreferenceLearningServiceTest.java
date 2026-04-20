package com.microsaas.contextlayer.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contextlayer.domain.CustomerPreference;
import com.microsaas.contextlayer.repository.CustomerPreferenceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PreferenceLearningServiceTest {
    @Mock
    private AiService aiService;
    @Mock
    private CustomerPreferenceRepository repository;
    @InjectMocks
    private PreferenceLearningService service;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() { TenantContext.set(tenantId); }

    @AfterEach
    void tearDown() { TenantContext.clear(); }

    @Test
    void testLearnPreference() {
        ChatResponse resp = new ChatResponse("id", "model", "contact_method:email", null);
        when(aiService.chat(any())).thenReturn(resp);
        when(repository.findByCustomerIdAndTenantIdAndPreferenceKey(eq("c1"), eq(tenantId), eq("contact_method")))
            .thenReturn(Optional.empty());

        CustomerPreference saved = new CustomerPreference();
        when(repository.save(any())).thenReturn(saved);

        service.learnPreferenceFromInteraction("c1", "Call me back via email");
        verify(repository).save(any());
    }
}
