package com.microsaas.documentvault.service;

import org.springframework.stereotype.Service;

@Service
public class AIService {
    public String performOCR(byte[] fileBytes) {
        // Mock LiteLLM call for OCR
        return "Mocked OCR content of the document. Important keywords: budget, report, legal.";
    }
}
