package com.microsaas.notificationhub.service;

import com.microsaas.notificationhub.api.dto.CreateTemplateRequest;
import com.microsaas.notificationhub.api.dto.TemplateDto;
import com.microsaas.notificationhub.domain.entity.NotificationTemplate;
import com.microsaas.notificationhub.domain.repository.NotificationTemplateRepository;
import com.microsaas.notificationhub.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final NotificationTemplateRepository templateRepository;

    @Transactional
    public TemplateDto createTemplate(String tenantId, CreateTemplateRequest request) {
        NotificationTemplate template = NotificationTemplate.builder()
                .id(UUID.randomUUID())
                .tenantId(tenantId)
                .name(request.getName())
                .description(request.getDescription())
                .channel(request.getChannel())
                .subjectTemplate(request.getSubjectTemplate())
                .contentTemplate(request.getContentTemplate())
                .build();

        templateRepository.save(template);
        return mapToDto(template);
    }

    @Transactional(readOnly = true)
    public List<TemplateDto> getTemplates(String tenantId) {
        return templateRepository.findByTenantId(tenantId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TemplateDto getTemplate(String tenantId, UUID id) {
        return templateRepository.findByIdAndTenantId(id, tenantId)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
    }

    @Transactional
    public TemplateDto updateTemplate(String tenantId, UUID id, CreateTemplateRequest request) {
        NotificationTemplate template = templateRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setChannel(request.getChannel());
        template.setSubjectTemplate(request.getSubjectTemplate());
        template.setContentTemplate(request.getContentTemplate());

        templateRepository.save(template);
        return mapToDto(template);
    }

    @Transactional
    public void deleteTemplate(String tenantId, UUID id) {
        NotificationTemplate template = templateRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
        templateRepository.delete(template);
    }

    @Transactional(readOnly = true)
    public String testTemplate(String tenantId, UUID id, com.microsaas.notificationhub.api.dto.TestTemplateRequest request) {
        NotificationTemplate template = templateRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        String content = template.getContentTemplate();
        if (request.getVariables() != null) {
            for (java.util.Map.Entry<String, String> entry : request.getVariables().entrySet()) {
                content = content.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
        }
        return content;
    }

    private TemplateDto mapToDto(NotificationTemplate template) {
        return TemplateDto.builder()
                .id(template.getId())
                .name(template.getName())
                .description(template.getDescription())
                .channel(template.getChannel())
                .subjectTemplate(template.getSubjectTemplate())
                .contentTemplate(template.getContentTemplate())
                .createdAt(template.getCreatedAt())
                .updatedAt(template.getUpdatedAt())
                .build();
    }
}
