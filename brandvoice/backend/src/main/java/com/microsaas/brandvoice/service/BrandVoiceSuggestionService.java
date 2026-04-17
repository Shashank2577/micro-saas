package com.microsaas.brandvoice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandVoiceSuggestionService {

    // Again, simulating LiteLLM. A real implementation would call http://localhost:4000/v1/chat/completions
    // Prompt: "Rewrite the following sentence to match this tone: [Tone]. Explain why the rewrite is better."

    public String suggestRewrite(String originalText, String desiredTone) {
        return "Alternative text matching a " + desiredTone + " tone instead of using '" + originalText + "'";
    }
}
