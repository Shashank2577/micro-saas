package com.marketplacehub.dto;

import com.marketplacehub.model.App;
import lombok.Data;

import java.util.List;

@Data
public class AiSearchResponse {
    private String recommendation;
    private List<App> matchingApps;
}
