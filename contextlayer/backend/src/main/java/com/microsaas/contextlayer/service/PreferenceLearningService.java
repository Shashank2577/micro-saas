package com.microsaas.contextlayer.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contextlayer.domain.CustomerPreference;
import com.microsaas.contextlayer.repository.CustomerPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreferenceLearningService {
    private final AiService aiService;
    private final CustomerPreferenceRepository customerPreferenceRepository;

    public void learnPreferenceFromInteraction(String customerId, String interactionContent) {
        String prompt = "Extract any customer preferences from the following interaction. Return ONLY a single preference key and value separated by a colon (e.g., contact_method:email). If none, return 'none'. Interaction: " + interactionContent;
        String response = aiService.chat(new ChatRequest("gpt-3.5-turbo", List.of(new ChatMessage("user", prompt)), null, null)).content();

        if (!"none".equalsIgnoreCase(response.trim()) && response.contains(":")) {
            String[] parts = response.split(":", 2);
            String key = parts[0].trim();
            String value = parts[1].trim();

            savePreference(customerId, key, value, "PreferenceLearningService");
        }
    }

    public CustomerPreference savePreference(String customerId, String key, String value, String sourceApp) {
        CustomerPreference pref = customerPreferenceRepository.findByCustomerIdAndTenantIdAndPreferenceKey(customerId, TenantContext.require(), key)
            .orElseGet(() -> {
                CustomerPreference newPref = new CustomerPreference();
                newPref.setCustomerId(customerId);
                newPref.setTenantId(TenantContext.require());
                newPref.setPreferenceKey(key);
                return newPref;
            });

        pref.setPreferenceValue(value);
        pref.setSourceApp(sourceApp);
        pref.setValidFrom(Instant.now());
        return customerPreferenceRepository.save(pref);
    }

    public List<CustomerPreference> getPreferences(String customerId) {
        return customerPreferenceRepository.findByCustomerIdAndTenantId(customerId, TenantContext.require());
    }
}
