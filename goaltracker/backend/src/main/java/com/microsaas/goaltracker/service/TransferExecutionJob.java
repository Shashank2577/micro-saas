package com.microsaas.goaltracker.service;

import com.microsaas.goaltracker.entity.AutomatedTransfer;
import com.microsaas.goaltracker.entity.Contribution;
import com.microsaas.goaltracker.repository.AutomatedTransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferExecutionJob {

    private final AutomatedTransferRepository transferRepository;
    private final ProgressTrackingService progressTrackingService;

    // Run every day at 1 AM
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void executeDueTransfers() {
        log.info("Starting execution of due automated transfers...");
        List<AutomatedTransfer> allTransfers = transferRepository.findAll();

        LocalDateTime now = LocalDateTime.now();

        for (AutomatedTransfer transfer : allTransfers) {
            if (!transfer.getPaused() && transfer.getStatus().equals("ACTIVE")) {
                if (transfer.getNextRunDate().isBefore(now) || transfer.getNextRunDate().isEqual(now)) {
                    log.info("Executing transfer for goal: " + transfer.getGoal().getId());

                    // Emulate execution (In reality, this would call Stripe/Plaid API)
                    Contribution contribution = Contribution.builder()
                            .amount(transfer.getAmount())
                            .type("automated")
                            .sourceAccountId(transfer.getSourceAccountId())
                            .destinationAccountId(transfer.getDestinationAccountId())
                            .build();

                    progressTrackingService.recordContribution(transfer.getGoal(), contribution);

                    // Update next run date
                    transfer.setNextRunDate(calculateNextRun(transfer.getFrequency(), transfer.getNextRunDate()));
                    transferRepository.save(transfer);
                }
            }
        }
        log.info("Completed execution of due automated transfers.");
    }

    private LocalDateTime calculateNextRun(String frequency, LocalDateTime lastRun) {
        return switch (frequency.toLowerCase()) {
            case "weekly" -> lastRun.plusWeeks(1);
            case "bi-weekly" -> lastRun.plusWeeks(2);
            case "monthly" -> lastRun.plusMonths(1);
            default -> lastRun.plusMonths(1);
        };
    }
}
