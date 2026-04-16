package com.microsaas.hiresignal.controller;

import com.microsaas.hiresignal.model.JobRequisition;
import com.microsaas.hiresignal.service.RequisitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/requisitions")
@RequiredArgsConstructor
public class RequisitionController {

    private final RequisitionService requisitionService;

    @PostMapping
    public ResponseEntity<JobRequisition> createReq(@RequestBody CreateReqRequest request) {
        return ResponseEntity.ok(requisitionService.createReq(request.title(), request.department(), request.requirements()));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<JobRequisition> closeReq(@PathVariable UUID id) {
        return ResponseEntity.ok(requisitionService.closeReq(id));
    }

    @GetMapping
    public ResponseEntity<List<JobRequisition>> listReqs() {
        return ResponseEntity.ok(requisitionService.listReqs());
    }

    public record CreateReqRequest(String title, String department, String requirements) {}
}
