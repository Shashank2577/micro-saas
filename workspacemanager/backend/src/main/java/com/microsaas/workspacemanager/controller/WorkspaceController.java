package com.microsaas.workspacemanager.controller;

import com.microsaas.workspacemanager.domain.Workspace;
import com.microsaas.workspacemanager.domain.SsoDomain;
import com.microsaas.workspacemanager.dto.WorkspaceDto;
import com.microsaas.workspacemanager.dto.SsoDomainDto;
import com.microsaas.workspacemanager.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<Workspace> createWorkspace(@RequestBody WorkspaceDto dto) {
        return ResponseEntity.ok(workspaceService.createWorkspace(dto));
    }

    @GetMapping
    public ResponseEntity<Workspace> getWorkspace() {
        return ResponseEntity.ok(workspaceService.getWorkspace());
    }

    @PutMapping
    public ResponseEntity<Workspace> updateWorkspace(@RequestBody WorkspaceDto dto) {
        return ResponseEntity.ok(workspaceService.updateWorkspace(dto));
    }

    @PostMapping("/suspend")
    public ResponseEntity<Workspace> suspendWorkspace() {
        return ResponseEntity.ok(workspaceService.suspendWorkspace());
    }

    @GetMapping("/sso-domains")
    public ResponseEntity<List<SsoDomain>> getSsoDomains() {
        return ResponseEntity.ok(workspaceService.getSsoDomains());
    }

    @PostMapping("/sso-domains")
    public ResponseEntity<SsoDomain> addSsoDomain(@RequestBody SsoDomainDto dto) {
        return ResponseEntity.ok(workspaceService.addSsoDomain(dto));
    }
}
