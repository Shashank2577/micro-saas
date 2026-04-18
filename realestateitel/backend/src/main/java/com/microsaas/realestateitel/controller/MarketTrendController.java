package com.microsaas.realestateitel.controller;

import com.microsaas.realestateitel.domain.MarketTrend;
import com.microsaas.realestateitel.service.MarketTrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market-trends")
@RequiredArgsConstructor
public class MarketTrendController {

    private final MarketTrendService marketTrendService;

    @GetMapping("/{zipCode}")
    public ResponseEntity<List<MarketTrend>> getMarketTrends(@PathVariable String zipCode) {
        return ResponseEntity.ok(marketTrendService.getMarketTrends(zipCode));
    }
}
