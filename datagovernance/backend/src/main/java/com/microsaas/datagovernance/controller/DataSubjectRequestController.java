package com.microsaas.datagovernance.controller;

import com.microsaas.datagovernance.model.DataSubjectRequest;
import com.microsaas.datagovernance.service.DataGovernanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dsar")
@Tag(name = "DSAR API", description = "Data Subject Access Requests")
public class DataSubjectRequestController {

    @Autowired
    private DataGovernanceService service;

    @GetMapping
    public List<DataSubjectRequest> getDsars() {
        return service.getDsars();
    }

    @PostMapping
    public DataSubjectRequest createDsar(@RequestBody DataSubjectRequest request) {
        return service.createDsar(request);
    }

    @PostMapping("/{id}/process")
    public DataSubjectRequest processDsar(@PathVariable UUID id) {
        return service.processDsar(id);
    }

    @Operation(summary = "Download DSAR export as ZIP")
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadExport(@PathVariable UUID id) {
        DataSubjectRequest request = service.getDsar(id);
        if (!"COMPLETED".equals(request.getStatus())) {
            return ResponseEntity.badRequest().build();
        }
        
        byte[] dummyZipContent = new byte[]{0x50, 0x4B, 0x03, 0x04};
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"export-" + id + ".zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(dummyZipContent);
    }
}
