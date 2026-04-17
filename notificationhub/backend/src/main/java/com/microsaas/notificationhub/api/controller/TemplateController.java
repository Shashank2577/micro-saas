package com.microsaas.notificationhub.api.controller;

import com.microsaas.notificationhub.api.dto.CreateTemplateRequest;
import com.microsaas.notificationhub.api.dto.TemplateDto;
import com.microsaas.notificationhub.service.TemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TemplateDto createTemplate(@RequestHeader("X-Tenant-ID") String tenantId,
                                      @Valid @RequestBody CreateTemplateRequest request) {
        return templateService.createTemplate(tenantId, request);
    }

    @GetMapping
    public List<TemplateDto> getTemplates(@RequestHeader("X-Tenant-ID") String tenantId) {
        return templateService.getTemplates(tenantId);
    }

    @GetMapping("/{templateId}")
    public TemplateDto getTemplate(@RequestHeader("X-Tenant-ID") String tenantId,
                                   @PathVariable UUID templateId) {
        return templateService.getTemplate(tenantId, templateId);
    }

    @PutMapping("/{templateId}")
    public TemplateDto updateTemplate(@RequestHeader("X-Tenant-ID") String tenantId,
                                      @PathVariable UUID templateId,
                                      @Valid @RequestBody CreateTemplateRequest request) {
        return templateService.updateTemplate(tenantId, templateId, request);
    }

    @DeleteMapping("/{templateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTemplate(@RequestHeader("X-Tenant-ID") String tenantId,
                               @PathVariable UUID templateId) {
        templateService.deleteTemplate(tenantId, templateId);
    }

    @PostMapping("/{templateId}/test")
    public String testTemplate(@RequestHeader("X-Tenant-ID") String tenantId,
                               @PathVariable UUID templateId,
                               @RequestBody com.microsaas.notificationhub.api.dto.TestTemplateRequest request) {
        return templateService.testTemplate(tenantId, templateId, request);
    }
}
