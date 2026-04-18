package com.microsaas.restaurantintel.controller;

import com.microsaas.restaurantintel.domain.CustomerReview;
import com.microsaas.restaurantintel.service.ReviewIntelligenceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewIntelligenceController {

    private final ReviewIntelligenceService service;

    public ReviewIntelligenceController(ReviewIntelligenceService service) {
        this.service = service;
    }

    @GetMapping
    public List<CustomerReview> getReviews() {
        return service.getReviews();
    }

    @PostMapping
    public CustomerReview createReview(@RequestBody CustomerReview review) {
        return service.createReview(review);
    }

    @PostMapping("/{id}/analyze")
    public CustomerReview analyzeReview(@PathVariable UUID id) {
        return service.analyzeReview(id);
    }
}
