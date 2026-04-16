package com.microsaas.performancenarrative.service;

import com.microsaas.performancenarrative.entity.EmployeeReview;
import com.microsaas.performancenarrative.entity.PeerFeedback;
import com.microsaas.performancenarrative.repository.EmployeeReviewRepository;
import com.microsaas.performancenarrative.repository.PeerFeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private EmployeeReviewRepository employeeReviewRepository;

    @Mock
    private PeerFeedbackRepository peerFeedbackRepository;

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private ReviewService reviewService;

    private EmployeeReview review;
    private UUID reviewId;

    @BeforeEach
    void setUp() {
        reviewId = UUID.randomUUID();
        review = new EmployeeReview();
        review.setId(reviewId);
        review.setEmployeeName("John Doe");
    }

    @Test
    void testCreateReviewSetsStatusToDraft() {
        when(employeeReviewRepository.save(any(EmployeeReview.class))).thenAnswer(i -> i.getArguments()[0]);

        EmployeeReview created = reviewService.createReview(review);

        assertEquals(EmployeeReview.ReviewStatus.DRAFT, created.getStatus());
        verify(employeeReviewRepository, times(1)).save(review);
    }

    @Test
    void testGenerateDraftNarrativeIncludesPeerCount() {
        PeerFeedback feedback1 = new PeerFeedback();
        feedback1.setRating(4);
        PeerFeedback feedback2 = new PeerFeedback();
        feedback2.setRating(5);
        List<PeerFeedback> feedbacks = List.of(feedback1, feedback2);

        when(employeeReviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(peerFeedbackRepository.findByReviewId(reviewId)).thenReturn(feedbacks);
        when(feedbackService.getAverageRating(reviewId)).thenReturn(4.5);
        when(employeeReviewRepository.save(any(EmployeeReview.class))).thenAnswer(i -> i.getArguments()[0]);

        EmployeeReview updatedReview = reviewService.generateDraftNarrative(reviewId);

        assertTrue(updatedReview.getDraftNarrative().contains("2 peer feedbacks"));
        assertTrue(updatedReview.getDraftNarrative().contains("4.50"));
    }

    @Test
    void testFinalizeReviewSetsStatusToFinalized() {
        when(employeeReviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(employeeReviewRepository.save(any(EmployeeReview.class))).thenAnswer(i -> i.getArguments()[0]);

        EmployeeReview finalized = reviewService.finalizeReview(reviewId);

        assertEquals(EmployeeReview.ReviewStatus.FINALIZED, finalized.getStatus());
    }
}
