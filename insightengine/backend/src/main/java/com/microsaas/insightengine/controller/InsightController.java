package com.microsaas.insightengine.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.insightengine.dto.AssignRequest;
import com.microsaas.insightengine.dto.CommentRequest;
import com.microsaas.insightengine.dto.StatusUpdateRequest;
import com.microsaas.insightengine.entity.Insight;
import com.microsaas.insightengine.entity.InsightComment;
import com.microsaas.insightengine.repository.InsightCommentRepository;
import com.microsaas.insightengine.repository.InsightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/insights")
@RequiredArgsConstructor
public class InsightController {

    private final InsightRepository insightRepository;
    private final InsightCommentRepository insightCommentRepository;

    @GetMapping
    public List<Insight> getInsights() {
        UUID tenantId = TenantContext.require();
        return insightRepository.findByTenantIdOrderByImpactScoreDesc(tenantId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insight> getInsight(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        return insightRepository.findByIdAndTenantId(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Insight> updateStatus(@PathVariable UUID id, @RequestBody StatusUpdateRequest request) {
        UUID tenantId = TenantContext.require();
        return insightRepository.findByIdAndTenantId(id, tenantId)
                .map(insight -> {
                    insight.setStatus(request.getStatus());
                    return ResponseEntity.ok(insightRepository.save(insight));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<Insight> assignInsight(@PathVariable UUID id, @RequestBody AssignRequest request) {
        UUID tenantId = TenantContext.require();
        return insightRepository.findByIdAndTenantId(id, tenantId)
                .map(insight -> {
                    insight.setAssignedTo(request.getAssignedTo());
                    return ResponseEntity.ok(insightRepository.save(insight));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/comments")
    public InsightComment addComment(@PathVariable UUID id, @RequestBody CommentRequest request) {
        UUID tenantId = TenantContext.require();
        InsightComment comment = new InsightComment();
        comment.setTenantId(tenantId);
        comment.setInsightId(id);
        comment.setUserId(UUID.randomUUID()); // In reality, get from security context
        comment.setCommentText(request.getText());
        return insightCommentRepository.save(comment);
    }

    @GetMapping("/{id}/comments")
    public List<InsightComment> getComments(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        return insightCommentRepository.findByInsightIdAndTenantIdOrderByCreatedAtAsc(id, tenantId);
    }
}
