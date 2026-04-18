package com.microsaas.regulatoryfiling.controller;

import com.microsaas.regulatoryfiling.domain.FilingObligation;
import com.microsaas.regulatoryfiling.service.FilingObligationService;
import com.microsaas.regulatoryfiling.dto.ValidationResult;
import com.microsaas.regulatoryfiling.dto.SimulationResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/filings/filing-obligations")
public class FilingObligationController {

    private final FilingObligationService service;

    public FilingObligationController(FilingObligationService service) {
        this.service = service;
    }

    @GetMapping
    public List<FilingObligation> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public FilingObligation getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilingObligation create(@RequestBody FilingObligation entity) {
        return service.create(entity);
    }

    @PatchMapping("/{id}")
    public FilingObligation update(@PathVariable UUID id, @RequestBody FilingObligation entity) {
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
