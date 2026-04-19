package com.microsaas.regulatoryfiling.controller;

import com.microsaas.regulatoryfiling.domain.JurisdictionSchedule;
import com.microsaas.regulatoryfiling.service.JurisdictionScheduleService;
import com.microsaas.regulatoryfiling.dto.ValidationResult;
import com.microsaas.regulatoryfiling.dto.SimulationResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/filings/jurisdiction-schedules")
public class JurisdictionScheduleController {

    private final JurisdictionScheduleService service;

    public JurisdictionScheduleController(JurisdictionScheduleService service) {
        this.service = service;
    }

    @GetMapping
    public List<JurisdictionSchedule> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public JurisdictionSchedule getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JurisdictionSchedule create(@RequestBody JurisdictionSchedule entity) {
        return service.create(entity);
    }

    @PatchMapping("/{id}")
    public JurisdictionSchedule update(@PathVariable UUID id, @RequestBody JurisdictionSchedule entity) {
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
