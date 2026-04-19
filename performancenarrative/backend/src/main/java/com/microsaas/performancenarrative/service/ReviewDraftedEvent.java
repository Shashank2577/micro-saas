package com.microsaas.performancenarrative.service;

import java.util.UUID;

public record ReviewDraftedEvent(UUID reviewId, UUID tenantId) {}
