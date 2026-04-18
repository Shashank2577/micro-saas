package com.microsaas.apimanager.client;

import org.springframework.stereotype.Component;

@Component
public class LiteLLMClient {
    public String getApiRecommendations(String schema) {
        return "Recommendations for schema:\n" + schema + "\n- Consider adding better descriptions.\n- Group paths into tags.";
    }
}
