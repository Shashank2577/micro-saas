package com.microsaas.socialintelligence.api;

import com.microsaas.socialintelligence.domain.model.TrendSignal;
import com.microsaas.socialintelligence.domain.repository.TrendSignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trends")
@RequiredArgsConstructor
public class TrendSignalController {

    private final TrendSignalRepository repository;

    @GetMapping
    public ResponseEntity<List<TrendSignal>> getEmergingTrends() {
        return ResponseEntity.ok(repository.findByStatus("EMERGING"));
    }
}
