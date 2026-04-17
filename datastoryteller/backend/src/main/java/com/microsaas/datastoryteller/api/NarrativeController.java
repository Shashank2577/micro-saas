package com.microsaas.datastoryteller.api;

import com.microsaas.datastoryteller.domain.model.Feedback;
import com.microsaas.datastoryteller.domain.model.NarrativeReport;
import com.microsaas.datastoryteller.service.NarrativeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/narratives")
@RequiredArgsConstructor
@Tag(name = "Narratives", description = "Narrative and Insight generation")
public class NarrativeController {

    private final NarrativeService narrativeService;

    @PostMapping("/generate")
    @Operation(summary = "Generate a narrative")
    public ResponseEntity<NarrativeReport> generateNarrative(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody GenerateRequest request) {
        return ResponseEntity.ok(narrativeService.generateNarrative(tenantId, request.getDatasetId(), request.getTemplateId(), request.getTimeRangeStart(), request.getTimeRangeEnd()));
    }

    @GetMapping
    @Operation(summary = "List narratives")
    public ResponseEntity<Page<NarrativeReport>> listNarratives(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam(required = false) UUID datasetId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(narrativeService.listNarratives(tenantId, datasetId, status, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detail")
    public ResponseEntity<NarrativeReport> getNarrative(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(narrativeService.getNarrative(id, tenantId));
    }

    @PutMapping("/{id}/publish")
    @Operation(summary = "Publish narrative")
    public ResponseEntity<NarrativeReport> publishNarrative(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(narrativeService.publishNarrative(id, tenantId));
    }

    @PostMapping("/{id}/feedback")
    @Operation(summary = "Submit feedback")
    public ResponseEntity<Feedback> addFeedback(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id, @RequestBody FeedbackRequest req) {
        return ResponseEntity.ok(narrativeService.addFeedback(id, tenantId, req.getRating(), req.getComment(), "current-user"));
    }

    @PostMapping("/ask")
    @Operation(summary = "Interactive Q&A")
    public ResponseEntity<QaResponse> askQuestion(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody QaRequest req) {
        String answer = narrativeService.askQuestion(tenantId, req.getDatasetId(), req.getQuestion());
        return ResponseEntity.ok(new QaResponse(answer, "SELECT * FROM dataset;"));
    }

    @PostMapping(value = "/chart-describe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Describe chart")
    public ResponseEntity<String> describeChart(@RequestHeader("X-Tenant-ID") String tenantId, @RequestParam("file") MultipartFile file) {
        try {
            String base64 = Base64.getEncoder().encodeToString(file.getBytes());
            return ResponseEntity.ok(narrativeService.describeChart(tenantId, base64));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

@Data
class GenerateRequest {
    private UUID datasetId;
    private UUID templateId;
    private OffsetDateTime timeRangeStart;
    private OffsetDateTime timeRangeEnd;
}

@Data
class FeedbackRequest {
    private int rating;
    private String comment;
}

@Data
class QaRequest {
    private UUID datasetId;
    private String question;
}

@Data
class QaResponse {
    private final String answer;
    private final String sql;
}
