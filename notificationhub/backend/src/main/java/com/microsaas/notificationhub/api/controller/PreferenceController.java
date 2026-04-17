package com.microsaas.notificationhub.api.controller;

import com.microsaas.notificationhub.api.dto.PreferenceDto;
import com.microsaas.notificationhub.api.dto.UpdatePreferenceRequest;
import com.microsaas.notificationhub.service.PreferenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/preferences")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;

    @GetMapping("/{userId}")
    public List<PreferenceDto> getUserPreferences(@RequestHeader("X-Tenant-ID") String tenantId,
                                                  @PathVariable String userId) {
        return preferenceService.getUserPreferences(tenantId, userId);
    }

    @PutMapping("/{userId}/{channel}")
    public PreferenceDto updatePreference(@RequestHeader("X-Tenant-ID") String tenantId,
                                          @PathVariable String userId,
                                          @PathVariable String channel,
                                          @Valid @RequestBody UpdatePreferenceRequest request) {
        return preferenceService.updatePreference(tenantId, userId, channel, request.getOptedIn());
    }

    @PostMapping("/{userId}/opt-out")
    public PreferenceDto optOut(@RequestHeader("X-Tenant-ID") String tenantId,
                                @PathVariable String userId,
                                @RequestBody String channel) {
        // channel is passed as a simple string body or adjust as needed, keeping simple for now
        return preferenceService.updatePreference(tenantId, userId, channel, false);
    }
}
