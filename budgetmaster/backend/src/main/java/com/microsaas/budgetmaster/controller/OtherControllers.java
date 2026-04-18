package com.microsaas.budgetmaster.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.budgetmaster.domain.FamilyMember;
import com.microsaas.budgetmaster.domain.IrregularExpense;
import com.microsaas.budgetmaster.domain.Target;
import com.microsaas.budgetmaster.dto.Requests.*;
import com.microsaas.budgetmaster.repository.FamilyMemberRepository;
import com.microsaas.budgetmaster.repository.IrregularExpenseRepository;
import com.microsaas.budgetmaster.repository.TargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OtherControllers {
    private final TargetRepository targetRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final IrregularExpenseRepository irregularExpenseRepository;

    @GetMapping("/targets")
    public ResponseEntity<List<Target>> getTargets() {
        return ResponseEntity.ok(targetRepository.findAllByTenantId(TenantContext.require()));
    }

    @PostMapping("/targets")
    public ResponseEntity<Target> createTarget(@RequestBody CreateTargetRequest request) {
        Target t = Target.builder()
                .tenantId(TenantContext.require())
                .name(request.getName())
                .targetAmount(request.getTargetAmount())
                .currentAmount(BigDecimal.ZERO)
                .deadline(request.getDeadline())
                .build();
        return ResponseEntity.ok(targetRepository.save(t));
    }

    @GetMapping("/family-members")
    public ResponseEntity<List<FamilyMember>> getFamilyMembers() {
        return ResponseEntity.ok(familyMemberRepository.findAllByTenantId(TenantContext.require()));
    }

    @PostMapping("/family-members")
    public ResponseEntity<FamilyMember> createFamilyMember(@RequestBody CreateFamilyMemberRequest request) {
        FamilyMember fm = FamilyMember.builder()
                .tenantId(TenantContext.require())
                .name(request.getName())
                .role(request.getRole())
                .individualAllowance(request.getIndividualAllowance())
                .build();
        return ResponseEntity.ok(familyMemberRepository.save(fm));
    }

    @GetMapping("/irregular-expenses")
    public ResponseEntity<List<IrregularExpense>> getIrregularExpenses() {
        return ResponseEntity.ok(irregularExpenseRepository.findAllByTenantId(TenantContext.require()));
    }

    @PostMapping("/irregular-expenses")
    public ResponseEntity<IrregularExpense> createIrregularExpense(@RequestBody CreateIrregularExpenseRequest request) {
        IrregularExpense ie = IrregularExpense.builder()
                .tenantId(TenantContext.require())
                .name(request.getName())
                .amount(request.getAmount())
                .dueDate(request.getDueDate())
                .frequency(request.getFrequency())
                .build();
        return ResponseEntity.ok(irregularExpenseRepository.save(ie));
    }
}
