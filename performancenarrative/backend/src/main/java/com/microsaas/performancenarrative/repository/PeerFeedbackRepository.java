package com.microsaas.performancenarrative.repository;

import com.microsaas.performancenarrative.entity.PeerFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public interface PeerFeedbackRepository extends JpaRepository<PeerFeedback, UUID> {
    List<PeerFeedback> findByReviewId(UUID reviewId);
}
