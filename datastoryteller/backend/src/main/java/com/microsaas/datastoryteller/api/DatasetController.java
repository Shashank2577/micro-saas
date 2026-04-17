package com.microsaas.datastoryteller.api;

import com.microsaas.datastoryteller.domain.model.Dataset;
import com.microsaas.datastoryteller.service.DataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/datasets")
@RequiredArgsConstructor
@Tag(name = "Datasets", description = "Register datasets")
public class DatasetController {

    private final DataService dataService;

    @PostMapping
    @Operation(summary = "Register dataset")
    public ResponseEntity<Dataset> registerDataset(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody Dataset dataset) {
        dataset.setTenantId(tenantId);
        return ResponseEntity.ok(dataService.registerDataset(dataset));
    }

    @GetMapping
    @Operation(summary = "List datasets")
    public ResponseEntity<List<Dataset>> listDatasets(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(dataService.listDatasets(tenantId));
    }

    @PostMapping("/{id}/preview")
    @Operation(summary = "Run SQL, return first 100 rows")
    public ResponseEntity<List<Map<String, Object>>> previewDataset(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(dataService.previewDataset(id, tenantId));
    }
}
