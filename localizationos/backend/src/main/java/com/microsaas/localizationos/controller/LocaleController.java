package com.microsaas.localizationos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/locales")
public class LocaleController {

    @GetMapping("/supported")
    public ResponseEntity<List<String>> getSupportedLocales() {
        // Return a basic list of supported locales as there's no specific LocaleService implemented yet
        return ResponseEntity.ok(Arrays.asList("en-US", "es-ES", "fr-FR", "de-DE", "ja-JP", "zh-CN"));
    }
}
