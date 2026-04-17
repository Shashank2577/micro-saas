package com.microsaas.notificationhub.service;

import com.microsaas.notificationhub.api.dto.NotificationDto;
import com.microsaas.notificationhub.api.dto.SendNotificationRequest;
import com.microsaas.notificationhub.domain.entity.Notification;
import com.microsaas.notificationhub.domain.entity.NotificationTemplate;
import com.microsaas.notificationhub.domain.repository.NotificationRepository;
import com.microsaas.notificationhub.domain.repository.NotificationTemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private NotificationTemplateRepository templateRepository;
    @Mock
    private PreferenceService preferenceService;
    @Mock
    private DeliveryService deliveryService;
    @Mock
    private ContentOptimizationService optimizationService;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendNotification() {
        UUID templateId = UUID.randomUUID();

        SendNotificationRequest request = new SendNotificationRequest();
        request.setUserId("user1");
        request.setTemplateId(templateId);
        request.setChannel("EMAIL");
        request.setVariables(Map.of("name", "Alice"));

        NotificationTemplate template = new NotificationTemplate();
        template.setId(templateId);
        template.setContentTemplate("Hello {{name}}");

        when(preferenceService.isUserOptedIn(anyString(), anyString(), anyString())).thenReturn(true);
        when(preferenceService.checkRateLimit(anyString(), anyString(), anyString())).thenReturn(true);
        when(templateRepository.findByIdAndTenantId(eq(templateId), anyString())).thenReturn(Optional.of(template));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(i -> i.getArguments()[0]);

        NotificationDto result = notificationService.sendNotification("tenant1", request);

        assertNotNull(result);
        assertEquals("Hello Alice", result.getContent());
        assertEquals("PENDING", result.getStatus());

        verify(deliveryService).deliverNotification(any(Notification.class));
    }
}
