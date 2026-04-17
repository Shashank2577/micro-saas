package com.crosscutting.starter.search;

import java.util.UUID;

public record SearchResult(
        String resourceType,
        UUID resourceId,
        String content,
        String metadata,
        double score
) {
}
