package com.microsaas.performancenarrative.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/performance/ai")
public class AiController {

    @PostMapping("/analyze")
    public Map<String, String> analyze(@RequestBody Map<String, Object> request) {
        return Map.of("result", "analysis complete", "insights", "Looks good.");
    }

    @PostMapping("/recommendations")
    public Map<String, String> recommendations(@RequestBody Map<String, Object> request) {
        return Map.of("recommendation", "Consider setting more stretch goals.");
    }
}
