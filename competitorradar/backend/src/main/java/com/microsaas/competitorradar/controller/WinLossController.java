package com.microsaas.competitorradar.controller;

import com.microsaas.competitorradar.dto.WinLossRecordDto;
import com.microsaas.competitorradar.service.WinLossService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/win-loss")
@RequiredArgsConstructor
public class WinLossController {

    private final WinLossService winLossService;

    @GetMapping
    public ResponseEntity<List<WinLossRecordDto>> getWinLossRecords() {
        return ResponseEntity.ok(winLossService.getWinLossRecords());
    }

    @PostMapping
    public ResponseEntity<WinLossRecordDto> recordWinLoss(@RequestBody WinLossRecordDto dto) {
        return ResponseEntity.ok(winLossService.recordWinLoss(dto));
    }
}
