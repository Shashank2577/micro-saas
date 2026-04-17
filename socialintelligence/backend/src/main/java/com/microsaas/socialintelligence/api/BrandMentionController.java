package com.microsaas.socialintelligence.api;

import com.microsaas.socialintelligence.domain.model.BrandMention;
import com.microsaas.socialintelligence.domain.repository.BrandMentionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mentions")
@RequiredArgsConstructor
public class BrandMentionController {

    private final BrandMentionRepository repository;

    @GetMapping
    public ResponseEntity<List<BrandMention>> getMentions(
            @RequestParam(required = false) String sentiment,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate) {

        return ResponseEntity.ok(repository.findWithFilters(sentiment, startDate, endDate));
    }
}
