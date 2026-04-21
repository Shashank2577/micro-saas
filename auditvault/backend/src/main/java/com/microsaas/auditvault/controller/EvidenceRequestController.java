package com.microsaas.auditvault.controller;

import com.microsaas.auditvault.model.EvidenceRequest;
import com.microsaas.auditvault.service.EvidenceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evidence-requests")
@RequiredArgsConstructor
public class EvidenceRequestController {
    private final EvidenceRequestService requestService;

    @GetMapping
    public List<EvidenceRequest> listRequests() {
        return requestService.listRequests();
    }
}
