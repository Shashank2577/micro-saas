package com.microsaas.regulatoryfiling.controller;

import com.microsaas.regulatoryfiling.domain.SubmissionReceipt;
import com.microsaas.regulatoryfiling.service.SubmissionReceiptService;
import com.microsaas.regulatoryfiling.dto.ValidationResult;
import com.microsaas.regulatoryfiling.dto.SimulationResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/filings/submission-receipts")
public class SubmissionReceiptController {

    private final SubmissionReceiptService service;

    public SubmissionReceiptController(SubmissionReceiptService service) {
        this.service = service;
    }

    @GetMapping
    public List<SubmissionReceipt> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public SubmissionReceipt getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubmissionReceipt create(@RequestBody SubmissionReceipt entity) {
        return service.create(entity);
    }

    @PatchMapping("/{id}")
    public SubmissionReceipt update(@PathVariable UUID id, @RequestBody SubmissionReceipt entity) {
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
