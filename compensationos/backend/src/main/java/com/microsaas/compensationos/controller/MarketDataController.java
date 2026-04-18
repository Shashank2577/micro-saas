package com.microsaas.compensationos.controller;

import com.microsaas.compensationos.dto.MarketDataDto;
import com.microsaas.compensationos.service.MarketDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market-data")
@RequiredArgsConstructor
@Tag(name = "Market Data")
public class MarketDataController {

    private final MarketDataService marketDataService;

    @GetMapping
    @Operation(summary = "Get market data")
    public ResponseEntity<List<MarketDataDto>> getMarketData(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String location) {
        return ResponseEntity.ok(marketDataService.getMarketData(role, location));
    }

    @PostMapping("/import")
    @Operation(summary = "Import market data")
    public ResponseEntity<Void> importMarketData(@RequestBody List<MarketDataDto> dtos) {
        marketDataService.importMarketData(dtos);
        return ResponseEntity.ok().build();
    }
}
