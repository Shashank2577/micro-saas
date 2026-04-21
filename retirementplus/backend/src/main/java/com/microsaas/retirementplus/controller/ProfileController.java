package com.microsaas.retirementplus.controller;

import com.microsaas.retirementplus.domain.UserProfile;
import com.microsaas.retirementplus.dto.ProfileDto;
import com.microsaas.retirementplus.service.ProfileService;
import com.crosscutting.starter.tenancy.TenantContext;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<UserProfile> createOrUpdateProfile(@Valid @RequestBody ProfileDto profileDto) {
        UUID tenantId = TenantContext.require();
        UserProfile profile = profileService.createOrUpdateProfile(profileDto, tenantId);
        return new ResponseEntity<>(profile, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getProfile(@PathVariable UUID userId) {
        UUID tenantId = TenantContext.require();
        return profileService.getProfileByUserId(userId, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
