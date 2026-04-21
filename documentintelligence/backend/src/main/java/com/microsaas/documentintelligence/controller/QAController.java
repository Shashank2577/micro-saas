package com.microsaas.documentintelligence.controller;

import com.microsaas.documentintelligence.dto.QAQuery;
import com.microsaas.documentintelligence.dto.QAResponse;
import com.microsaas.documentintelligence.service.QAService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/qa")
@RequiredArgsConstructor
@Tag(name = "QA", description = "Document QA API")
public class QAController {

    private final QAService qaService;

    @PostMapping("/{documentId}")
    @Operation(summary = "Ask question about a document")
    public ResponseEntity<QAResponse> askQuestion(@PathVariable UUID documentId, @RequestBody QAQuery query) {
        return ResponseEntity.ok(qaService.answerQuestion(documentId, query));
    }
}
