package com.marketplacehub.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.marketplacehub.dto.AiSearchResponse;
import com.marketplacehub.model.App;
import com.marketplacehub.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MarketplaceAiService {

    @Autowired
    private AppRepository appRepository;

    public AiSearchResponse aiSearch(String query) {
        UUID tenantId = TenantContext.require();
        // In a real scenario, this would integrate with LiteLLM and Elasticsearch.
        // For the autonomous challenge, providing a stubbed implementation of AI recommendation.
        List<App> apps = appRepository.searchApps(tenantId, query);
        
        AiSearchResponse response = new AiSearchResponse();
        response.setMatchingApps(apps);
        response.setRecommendation("Based on your query: '" + query + "', we recommend these apps.");
        return response;
    }
}
