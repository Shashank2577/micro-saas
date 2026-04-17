package com.microsaas.callintelligence.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.callintelligence.domain.call.Call;
import com.microsaas.callintelligence.domain.call.CallRepository;
import com.microsaas.callintelligence.domain.call.CallStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CallServiceTest {

    @Mock
    private CallRepository callRepository;
    
    @Mock
    private AnalysisService analysisService;

    @InjectMocks
    private CallService callService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testCreateCall() {
        Call call = Call.builder()
                .id(UUID.randomUUID())
                .tenantId(tenantId)
                .title("Discovery Call")
                .status(CallStatus.UPLOADING)
                .build();
        
        when(callRepository.save(any(Call.class))).thenReturn(call);

        Call result = callService.createCall("Discovery Call", "rep-1", "http://audio.url", 300);

        assertNotNull(result);
        assertEquals("Discovery Call", result.getTitle());
        verify(callRepository, times(1)).save(any(Call.class));
    }
    
    @Test
    void testStartAnalysis() {
        UUID callId = UUID.randomUUID();
        Call call = Call.builder()
                .id(callId)
                .tenantId(tenantId)
                .title("Discovery Call")
                .status(CallStatus.UPLOADING)
                .build();
                
        when(callRepository.findByIdAndTenantId(callId, tenantId)).thenReturn(Optional.of(call));
        
        callService.startAnalysis(callId);
        
        verify(callRepository).save(call);
        assertEquals(CallStatus.TRANSCRIBING, call.getStatus());
        verify(analysisService).analyzeCallAsync(callId, tenantId);
    }
}
