package com.microsaas.prospectiq.controller;

import com.microsaas.prospectiq.dto.ICPProfileRequest;
import com.microsaas.prospectiq.model.ICPProfile;
import com.microsaas.prospectiq.service.ICPProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/icp")
@RequiredArgsConstructor
@Tag(name = "ICP Profiles")
public class ICPProfileController {

    private final ICPProfileService icpProfileService;

    @GetMapping
    @Operation(summary = "List ICP profiles")
    public ResponseEntity<List<ICPProfile>> listProfiles() {
        return ResponseEntity.ok(icpProfileService.getAllProfiles());
    }

    @PostMapping
    @Operation(summary = "Create an ICP profile")
    public ResponseEntity<ICPProfile> createProfile(@RequestBody ICPProfileRequest request) {
        return ResponseEntity.ok(icpProfileService.createProfile(request));
    }
}
