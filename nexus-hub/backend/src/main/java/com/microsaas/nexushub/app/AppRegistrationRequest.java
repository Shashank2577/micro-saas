package com.microsaas.nexushub.app;

import java.util.Map;

public record AppRegistrationRequest(
        String name,
        String displayName,
        String baseUrl,
        Map<String, Object> manifest
) {}
