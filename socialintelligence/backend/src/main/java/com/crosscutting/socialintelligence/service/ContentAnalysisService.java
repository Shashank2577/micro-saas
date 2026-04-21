package com.crosscutting.socialintelligence.service;

import com.crosscutting.socialintelligence.domain.ContentPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentAnalysisService {
    public List<ContentPost> getTopContent(UUID tenantId, String platform, int limit) {
        return List.of();
    }
}
