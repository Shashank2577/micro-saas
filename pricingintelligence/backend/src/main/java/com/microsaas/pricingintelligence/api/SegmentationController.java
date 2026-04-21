package com.microsaas.pricingintelligence.api;

import com.microsaas.pricingintelligence.domain.CustomerSegment;
import com.microsaas.pricingintelligence.service.SegmentationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/segmentation")
@RequiredArgsConstructor
public class SegmentationController {

    private final SegmentationService segmentationService;

    @GetMapping
    public List<CustomerSegment> getSegments() {
        return segmentationService.getSegments();
    }
}
