package com.microsaas.socialintelligence.api;

import com.microsaas.socialintelligence.domain.model.CompetitorActivity;
import com.microsaas.socialintelligence.domain.repository.CompetitorActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/competitors")
@RequiredArgsConstructor
public class CompetitorActivityController {

    private final CompetitorActivityRepository repository;

    @GetMapping("/{handle}/activity")
    public ResponseEntity<List<CompetitorActivity>> getCompetitorActivity(@PathVariable String handle) {
        return ResponseEntity.ok(repository.findByCompetitorHandle(handle));
    }
}
