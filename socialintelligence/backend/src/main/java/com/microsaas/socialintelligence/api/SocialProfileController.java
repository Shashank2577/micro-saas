package com.microsaas.socialintelligence.api;

import com.microsaas.socialintelligence.domain.model.SocialProfile;
import com.microsaas.socialintelligence.domain.repository.SocialProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class SocialProfileController {

    private final SocialProfileRepository repository;

    @PostMapping
    public ResponseEntity<SocialProfile> createProfile(@RequestBody SocialProfile profile) {
        if (profile.getId() == null) {
            profile.setId(UUID.randomUUID());
        }
        return ResponseEntity.ok(repository.save(profile));
    }

    @GetMapping
    public ResponseEntity<List<SocialProfile>> getProfiles() {
        return ResponseEntity.ok(repository.findAll());
    }
}
