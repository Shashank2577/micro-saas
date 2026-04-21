package com.microsaas.auditvault.controller;

import com.microsaas.auditvault.model.ComplianceFinding;
import com.microsaas.auditvault.service.ComplianceFindingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compliance-findings")
@RequiredArgsConstructor
public class ComplianceFindingController {
    private final ComplianceFindingService findingService;

    @GetMapping
    public List<ComplianceFinding> listFindings() {
        return findingService.listFindings();
    }
}
