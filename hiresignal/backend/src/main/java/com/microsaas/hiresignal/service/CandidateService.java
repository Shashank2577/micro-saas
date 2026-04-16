package com.microsaas.hiresignal.service;

import com.crosscutting.event.EventPublisher;
import com.microsaas.hiresignal.model.Candidate;
import com.microsaas.hiresignal.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository repository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Candidate addCandidate(String name, String email, Candidate.Source source, UUID requisitionId) {
        Candidate candidate = Candidate.builder()
                .name(name)
                .email(email)
                .source(source)
                .requisitionId(requisitionId)
                .status(Candidate.Status.NEW)
                .build();
        return repository.save(candidate);
    }

    @Transactional
    public Candidate scoreFit(UUID candidateId) {
        Candidate candidate = repository.findById(candidateId).orElseThrow();
        int score = switch (candidate.getSource()) {
            case REFERRAL -> 85;
            case DIRECT -> 75;
            case LINKEDIN -> 70;
            case JOB_BOARD -> 60;
        };
        candidate.setFitScore(score);
        return repository.save(candidate);
    }

    @Transactional
    public Candidate advance(UUID candidateId) {
        Candidate candidate = repository.findById(candidateId).orElseThrow();
        
        switch (candidate.getStatus()) {
            case NEW -> candidate.setStatus(Candidate.Status.SCREENING);
            case SCREENING -> candidate.setStatus(Candidate.Status.INTERVIEWING);
            case INTERVIEWING -> candidate.setStatus(Candidate.Status.OFFERED);
            case OFFERED -> {
                candidate.setStatus(Candidate.Status.HIRED);
                eventPublisher.publish("candidate.hired", Map.of(
                        "candidateId", candidate.getId().toString(),
                        "requisitionId", candidate.getRequisitionId().toString()
                ));
            }
            case HIRED, REJECTED -> throw new IllegalStateException("Cannot advance candidate in status: " + candidate.getStatus());
        }
        
        return repository.save(candidate);
    }

    @Transactional
    public Candidate reject(UUID candidateId) {
        Candidate candidate = repository.findById(candidateId).orElseThrow();
        candidate.setStatus(Candidate.Status.REJECTED);
        return repository.save(candidate);
    }

    public List<Candidate> listByReq(UUID requisitionId) {
        return repository.findByRequisitionId(requisitionId);
    }
}
