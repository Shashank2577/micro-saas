package com.microsaas.performancenarrative.controller;

import com.microsaas.performancenarrative.entity.CalibrationNote;
import com.microsaas.performancenarrative.service.CalibrationNoteService;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/v1/performance/calibration-notes")
public class CalibrationNoteController {

    private final CalibrationNoteService service;

    public CalibrationNoteController(CalibrationNoteService service) {
        this.service = service;
    }

    @GetMapping
    public List<CalibrationNote> list() {
        return service.list();
    }

    @PostMapping
    public CalibrationNote create(@RequestBody CalibrationNote entity) {
        return service.create(entity);
    }

    @GetMapping("/{id}")
    public CalibrationNote getById(@PathVariable UUID id) {
        return service.getById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @PatchMapping("/{id}")
    public CalibrationNote update(@PathVariable UUID id, @RequestBody CalibrationNote details) {
        return service.update(id, details);
    }

    @PostMapping("/{id}/validate")
    public boolean validate(@PathVariable UUID id) {
        return service.validate(id);
    }
}
