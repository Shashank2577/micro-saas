package com.microsaas.datacatalogai.api;

import com.microsaas.datacatalogai.domain.model.QueryLog;
import com.microsaas.datacatalogai.service.QueryLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/query-logs")
@RequiredArgsConstructor
@Tag(name = "Query Logs", description = "Endpoints for ingesting query logs")
public class QueryLogController {

    private final QueryLogService queryLogService;

    @PostMapping("/ingest")
    @Operation(summary = "Batch ingest query logs")
    public void ingestLogs(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody List<QueryLog> logs) {
        queryLogService.ingestLogs(tenantId, logs);
    }
}
