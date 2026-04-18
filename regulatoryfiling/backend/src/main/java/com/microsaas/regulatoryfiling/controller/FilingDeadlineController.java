package com.microsaas.regulatoryfiling.controller;

import com.microsaas.regulatoryfiling.domain.FilingDeadline;
import com.microsaas.regulatoryfiling.service.FilingDeadlineService;
import com.microsaas.regulatoryfiling.dto.ValidationResult;
import com.microsaas.regulatoryfiling.dto.SimulationResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/filings/filing-deadlines")
public class FilingDeadlineController {

    private final FilingDeadlineService service;

    public FilingDeadlineController(FilingDeadlineService service) {
        this.service = service;
    }

    @GetMapping
    public List<FilingDeadline> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public FilingDeadline getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilingDeadline create(@RequestBody FilingDeadline entity) {
        return service.create(entity);
    }

    @PatchMapping("/{id}")
    public FilingDeadline update(@PathVariable UUID id, @RequestBody FilingDeadline entity) {
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
