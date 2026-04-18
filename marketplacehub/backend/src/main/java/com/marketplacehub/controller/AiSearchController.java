package com.marketplacehub.controller;

import com.marketplacehub.dto.AiSearchRequest;
import com.marketplacehub.dto.AiSearchResponse;
import com.marketplacehub.service.MarketplaceAiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/apps")
@Tag(name = "AI Search", description = "AI-powered search endpoints")
public class AiSearchController {

    @Autowired
    private MarketplaceAiService marketplaceAiService;

    @PostMapping("/search")
    @Operation(summary = "Perform AI-powered app search")
    public AiSearchResponse search(@RequestBody AiSearchRequest request) {
        return marketplaceAiService.aiSearch(request.getQuery());
    }
}
