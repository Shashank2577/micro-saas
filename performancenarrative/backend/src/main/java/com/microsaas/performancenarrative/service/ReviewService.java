package com.microsaas.performancenarrative.service;

import com.microsaas.performancenarrative.entity.EmployeeReview;
import com.microsaas.performancenarrative.entity.PeerFeedback;
import com.microsaas.performancenarrative.repository.EmployeeReviewRepository;
import com.microsaas.performancenarrative.repository.PeerFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final EmployeeReviewRepository employeeReviewRepository;
    private final PeerFeedbackRepository peerFeedbackRepository;
    private final FeedbackService feedbackService;

    @Transactional
    public EmployeeReview createReview(EmployeeReview review) {
        review.setStatus(EmployeeReview.ReviewStatus.DRAFT);
        return employeeReviewRepository.save(review);
    }

    @Transactional
    public EmployeeReview generateDraftNarrative(UUID reviewId) {
        EmployeeReview review = employeeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        
        List<PeerFeedback> feedbacks = peerFeedbackRepository.findByReviewId(reviewId);
        double averageRating = feedbackService.getAverageRating(reviewId);
        
        String narrativeTemplate = String.format(
                "Based on %d peer feedbacks, the average rating is %.2f.", 
                feedbacks.size(), 
                averageRating
        );
        
        review.setDraftNarrative(narrativeTemplate);
        return employeeReviewRepository.save(review);
    }

    @Transactional
    public EmployeeReview finalizeReview(UUID reviewId) {
        EmployeeReview review = employeeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        
        if (review.getDraftNarrative() != null && review.getFinalNarrative() == null) {
            review.setFinalNarrative(review.getDraftNarrative());
        }
        
        review.setStatus(EmployeeReview.ReviewStatus.FINALIZED);
        return employeeReviewRepository.save(review);
    }
}
