package com.microsaas.engagementpulse.controller;

import com.microsaas.engagementpulse.dto.SurveyResponseDto;
import com.microsaas.engagementpulse.model.SurveyResponse;
import com.microsaas.engagementpulse.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Responses", description = "Survey Responses API")
@RequestMapping("/api/responses")
public class ResponseController {

    private final ResponseService responseService;

    @Autowired
    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @PostMapping
    public ResponseEntity<SurveyResponse> submitResponse(@RequestBody SurveyResponseDto dto) {
        return ResponseEntity.ok(responseService.submitResponse(dto));
    }
}
