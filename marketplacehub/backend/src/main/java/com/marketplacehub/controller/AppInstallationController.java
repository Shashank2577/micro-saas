package com.marketplacehub.controller;

import com.marketplacehub.dto.InstallRequest;
import com.marketplacehub.model.AppInstallation;
import com.marketplacehub.service.MarketplaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "App Installation API", description = "Endpoints for app installations")
public class AppInstallationController {

    @Autowired
    private MarketplaceService marketplaceService;

    @PostMapping("/apps/{id}/install")
    @Operation(summary = "Install an app")
    public AppInstallation installApp(@PathVariable UUID id, @RequestBody InstallRequest request) {
        return marketplaceService.installApp(id, request);
    }

    @GetMapping("/installations/workspace/{workspaceId}")
    @Operation(summary = "Get installations for a workspace")
    public List<AppInstallation> getInstallations(@PathVariable String workspaceId) {
        return marketplaceService.getWorkspaceInstallations(workspaceId);
    }
}
