package com.microsaas.performancenarrative.service;

import java.util.UUID;

public record ReviewFinalizedEvent(UUID reviewId, UUID tenantId) {}
