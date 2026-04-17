package com.microsaas.datastoryteller.api;

import com.microsaas.datastoryteller.domain.model.DataSource;
import com.microsaas.datastoryteller.service.DataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/data-sources")
@RequiredArgsConstructor
@Tag(name = "Data Sources", description = "Manage database/warehouse connections")
public class DataSourceController {

    private final DataService dataService;

    @PostMapping
    @Operation(summary = "Create connection")
    public ResponseEntity<DataSource> createDataSource(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody DataSource dataSource) {
        dataSource.setTenantId(tenantId);
        return ResponseEntity.ok(dataService.createDataSource(dataSource));
    }

    @GetMapping
    @Operation(summary = "List data sources")
    public ResponseEntity<List<DataSource>> listDataSources(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(dataService.listDataSources(tenantId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detail (secrets redacted)")
    public ResponseEntity<DataSource> getDataSource(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        DataSource ds = dataService.getDataSource(id, tenantId);
        ds.setEncryptedConnection("********");
        return ResponseEntity.ok(ds);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete connection")
    public ResponseEntity<Void> deleteDataSource(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        dataService.deleteDataSource(id, tenantId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/test")
    @Operation(summary = "Connectivity probe")
    public ResponseEntity<Boolean> testConnection(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(dataService.testConnection(id, tenantId));
    }
}
