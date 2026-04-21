package com.microsaas.dealbrain.service;

import com.microsaas.dealbrain.model.DealActivity;
import com.microsaas.dealbrain.repository.DealActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailEngagementServiceTest {

    @Mock
    private DealActivityRepository activityRepository;

    @InjectMocks
    private EmailEngagementService service;

    @Test
    void logEmailEngagement_ShouldSaveActivity() {
        UUID tenantId = UUID.randomUUID();
        UUID dealId = UUID.randomUUID();

        service.logEmailEngagement(tenantId, dealId, "Follow up email sent.");

        verify(activityRepository).save(any(DealActivity.class));
    }
}
