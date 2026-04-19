package com.microsaas.regulatoryfiling.controller;

import com.microsaas.regulatoryfiling.domain.SubmissionPacket;
import com.microsaas.regulatoryfiling.service.SubmissionPacketService;
import com.microsaas.regulatoryfiling.dto.ValidationResult;
import com.microsaas.regulatoryfiling.dto.SimulationResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/filings/submission-packets")
public class SubmissionPacketController {

    private final SubmissionPacketService service;

    public SubmissionPacketController(SubmissionPacketService service) {
        this.service = service;
    }

    @GetMapping
    public List<SubmissionPacket> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public SubmissionPacket getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubmissionPacket create(@RequestBody SubmissionPacket entity) {
        return service.create(entity);
    }

    @PatchMapping("/{id}")
    public SubmissionPacket update(@PathVariable UUID id, @RequestBody SubmissionPacket entity) {
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
