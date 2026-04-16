package com.microsaas.performancenarrative.service;

import com.microsaas.performancenarrative.entity.PeerFeedback;
import com.microsaas.performancenarrative.repository.PeerFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final PeerFeedbackRepository peerFeedbackRepository;

    public double getAverageRating(UUID reviewId) {
        List<PeerFeedback> feedbacks = peerFeedbackRepository.findByReviewId(reviewId);
        if (feedbacks.isEmpty()) {
            return 0.0;
        }
        return feedbacks.stream()
                .mapToInt(PeerFeedback::getRating)
                .average()
                .orElse(0.0);
    }
}
