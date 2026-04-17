package com.microsaas.dataqualityai.controller;

import com.microsaas.dataqualityai.model.Dataset;
import com.microsaas.dataqualityai.repository.DatasetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/datasets")
@RequiredArgsConstructor
public class DatasetController {

    private final DatasetRepository datasetRepository;

    @PostMapping
    public ResponseEntity<Dataset> createDataset(@RequestBody Dataset dataset) {
        if(dataset.getTenantId() == null) {
            dataset.setTenantId("default");
        }
        return ResponseEntity.ok(datasetRepository.save(dataset));
    }

    @GetMapping
    public ResponseEntity<List<Dataset>> getAllDatasets() {
        return ResponseEntity.ok(datasetRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dataset> getDatasetById(@PathVariable String id) {
        return datasetRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
