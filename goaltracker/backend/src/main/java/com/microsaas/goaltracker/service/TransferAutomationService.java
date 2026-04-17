package com.microsaas.goaltracker.service;

import com.microsaas.goaltracker.entity.AutomatedTransfer;
import com.microsaas.goaltracker.entity.Goal;
import com.microsaas.goaltracker.repository.AutomatedTransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransferAutomationService {

    private final AutomatedTransferRepository transferRepository;

    public AutomatedTransfer setupTransfer(Goal goal, AutomatedTransfer transfer) {
        transfer.setGoal(goal);
        transfer.setStatus("ACTIVE");
        transfer.setPaused(false);
        transfer.setNextRunDate(calculateNextRun(transfer.getFrequency()));
        return transferRepository.save(transfer);
    }

    private LocalDateTime calculateNextRun(String frequency) {
        return switch (frequency.toLowerCase()) {
            case "weekly" -> LocalDateTime.now().plusWeeks(1);
            case "bi-weekly" -> LocalDateTime.now().plusWeeks(2);
            case "monthly" -> LocalDateTime.now().plusMonths(1);
            default -> LocalDateTime.now().plusMonths(1);
        };
    }
}
