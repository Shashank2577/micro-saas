package com.microsaas.dataroomai.service;

import com.microsaas.dataroomai.domain.AIAnswer;
import com.microsaas.dataroomai.repository.AIAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AIAnswerService {

    private final AIAnswerRepository aiAnswerRepository;

    @Transactional(readOnly = true)
    public List<AIAnswer> getAIAnswers(UUID roomId, UUID tenantId) {
        return aiAnswerRepository.findByRoomIdAndTenantId(roomId, tenantId);
    }

    @Transactional
    public AIAnswer createAIAnswer(AIAnswer aiAnswer) {
        return aiAnswerRepository.save(aiAnswer);
    }
}
