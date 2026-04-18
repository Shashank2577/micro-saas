package com.microsaas.dataunification.controller;

import com.microsaas.dataunification.model.AuditLog;
import com.microsaas.dataunification.service.AuditLogService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {
    private final AuditLogService service;

    public AuditLogController(AuditLogService service) {
        this.service = service;
    }

    @GetMapping
    public List<AuditLog> getAll() {
        return service.findAll();
    }
}
