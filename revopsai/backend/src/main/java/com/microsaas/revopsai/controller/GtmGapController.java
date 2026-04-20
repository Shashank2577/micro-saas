package com.microsaas.revopsai.controller;

import com.microsaas.revopsai.model.GtmGap;
import com.microsaas.revopsai.service.GtmGapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gtm-gaps")
@RequiredArgsConstructor
public class GtmGapController {
    private final GtmGapService service;

    @GetMapping
    public ResponseEntity<List<GtmGap>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<GtmGap> create(@RequestBody GtmGap entity) {
        return ResponseEntity.ok(service.create(entity));
    }
}
