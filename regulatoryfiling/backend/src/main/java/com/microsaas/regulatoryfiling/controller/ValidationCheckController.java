package com.microsaas.regulatoryfiling.controller;

import com.microsaas.regulatoryfiling.domain.ValidationCheck;
import com.microsaas.regulatoryfiling.service.ValidationCheckService;
import com.microsaas.regulatoryfiling.dto.ValidationResult;
import com.microsaas.regulatoryfiling.dto.SimulationResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/filings/validation-checks")
public class ValidationCheckController {

    private final ValidationCheckService service;

    public ValidationCheckController(ValidationCheckService service) {
        this.service = service;
    }

    @GetMapping
    public List<ValidationCheck> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public ValidationCheck getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ValidationCheck create(@RequestBody ValidationCheck entity) {
        return service.create(entity);
    }

    @PatchMapping("/{id}")
    public ValidationCheck update(@PathVariable UUID id, @RequestBody ValidationCheck entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PostMapping("/{id}/validate")
    public ValidationResult validate(@PathVariable UUID id) {
        return service.validate(id);
    }

    @PostMapping("/{id}/simulate")
    public SimulationResult simulate(@PathVariable UUID id) {
        return service.simulate(id);
    }
}
