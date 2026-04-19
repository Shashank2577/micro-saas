package com.microsaas.performancenarrative.service;

import java.util.UUID;

public record CalibrationFlaggedEvent(UUID calibrationId, UUID tenantId) {}
