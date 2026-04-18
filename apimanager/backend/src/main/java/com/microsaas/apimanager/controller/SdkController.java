package com.microsaas.apimanager.controller;

import com.microsaas.apimanager.service.SdkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/versions/{versionId}/sdk")
@RequiredArgsConstructor
public class SdkController {

    private final SdkService sdkService;

    @PostMapping
    public ResponseEntity<String> generateSdk(@PathVariable UUID projectId, @PathVariable UUID versionId, @RequestParam String language) {
        try {
            String sdkContent = sdkService.generateSdk(versionId, language);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=sdk-" + language + ".zip");
            return new ResponseEntity<>(sdkContent, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
