package com.microsaas.datacatalogai.api;

import com.microsaas.datacatalogai.domain.model.DataSource;
import com.microsaas.datacatalogai.service.DataSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sources")
@RequiredArgsConstructor
@Tag(name = "Data Sources", description = "Endpoints for managing data sources and triggering crawls")
public class DataSourceController {

    private final DataSourceService dataSourceService;

    @PostMapping
    @Operation(summary = "Register a new data source")
    public DataSource registerSource(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody DataSource source) {
        return dataSourceService.registerSource(tenantId, source);
    }

    @GetMapping
    @Operation(summary = "List data sources")
    public List<DataSource> listSources(@RequestHeader("X-Tenant-ID") String tenantId) {
        return dataSourceService.listSources(tenantId);
    }

    @PostMapping("/{id}/crawl")
    @Operation(summary = "Trigger a full crawl of a data source")
    public void crawlSource(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        dataSourceService.crawlSource(tenantId, id);
    }
}
