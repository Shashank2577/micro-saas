package com.microsaas.notificationhub.service;

import com.microsaas.notificationhub.api.dto.CreateTemplateRequest;
import com.microsaas.notificationhub.api.dto.TemplateDto;
import com.microsaas.notificationhub.domain.entity.NotificationTemplate;
import com.microsaas.notificationhub.domain.repository.NotificationTemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class TemplateServiceTest {

    @Mock
    private NotificationTemplateRepository templateRepository;

    @InjectMocks
    private TemplateService templateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTemplate() {
        CreateTemplateRequest request = new CreateTemplateRequest();
        request.setName("Test Template");
        request.setChannel("EMAIL");
        request.setContentTemplate("Hello {{name}}");

        when(templateRepository.save(any(NotificationTemplate.class))).thenAnswer(i -> i.getArguments()[0]);

        TemplateDto result = templateService.createTemplate("tenant1", request);

        assertNotNull(result);
        assertEquals("Test Template", result.getName());
        assertEquals("EMAIL", result.getChannel());
        verify(templateRepository).save(any(NotificationTemplate.class));
    }
}
