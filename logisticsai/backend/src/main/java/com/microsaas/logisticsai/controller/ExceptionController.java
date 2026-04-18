package com.microsaas.logisticsai.controller;

import com.microsaas.logisticsai.domain.LogisticsException;
import com.microsaas.logisticsai.service.ExceptionAgentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exceptions")
public class ExceptionController {

    private final ExceptionAgentService service;

    public ExceptionController(ExceptionAgentService service) {
        this.service = service;
    }

    @GetMapping
    public List<LogisticsException> getAllExceptions() {
        return service.getAllExceptions();
    }

    @PostMapping
    public LogisticsException reportException(@RequestBody LogisticsException exception) {
        return service.reportException(exception);
    }

    @PutMapping("/{id}/resolve")
    public LogisticsException resolveException(@PathVariable UUID id) {
        return service.resolveException(id);
    }
}
