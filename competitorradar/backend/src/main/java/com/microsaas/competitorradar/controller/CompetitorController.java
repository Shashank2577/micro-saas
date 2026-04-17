package com.microsaas.competitorradar.controller;

import com.microsaas.competitorradar.dto.CompetitorDto;
import com.microsaas.competitorradar.dto.BattlecardDto;
import com.microsaas.competitorradar.service.CompetitorService;
import com.microsaas.competitorradar.service.BattlecardGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/competitors")
@RequiredArgsConstructor
public class CompetitorController {

    private final CompetitorService competitorService;
    private final BattlecardGeneratorService battlecardGeneratorService;

    @GetMapping
    public ResponseEntity<List<CompetitorDto>> getCompetitors() {
        return ResponseEntity.ok(competitorService.getCompetitors());
    }

    @PostMapping
    public ResponseEntity<CompetitorDto> addCompetitor(@RequestBody CompetitorDto dto) {
        return ResponseEntity.ok(competitorService.addCompetitor(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCompetitor(@PathVariable UUID id) {
        competitorService.removeCompetitor(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/battlecard")
    public ResponseEntity<BattlecardDto> getBattlecard(@PathVariable UUID id) {
        BattlecardDto battlecard = battlecardGeneratorService.getLatestBattlecard(id);
        return battlecard != null ? ResponseEntity.ok(battlecard) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/battlecard/generate")
    public ResponseEntity<BattlecardDto> generateBattlecard(@PathVariable UUID id) {
        return ResponseEntity.ok(battlecardGeneratorService.generateBattlecard(id));
    }

    @GetMapping("/{id}/product-changes")
    public ResponseEntity<List<com.microsaas.competitorradar.dto.ProductChangeDto>> getProductChanges(@PathVariable java.util.UUID id, @org.springframework.beans.factory.annotation.Autowired com.microsaas.competitorradar.service.MonitoringService monitoringService) {
        return ResponseEntity.ok(monitoringService.getProductChanges(id));
    }

    @GetMapping("/{id}/pricing-changes")
    public ResponseEntity<List<com.microsaas.competitorradar.dto.PricingChangeDto>> getPricingChanges(@PathVariable java.util.UUID id, @org.springframework.beans.factory.annotation.Autowired com.microsaas.competitorradar.service.MonitoringService monitoringService) {
        return ResponseEntity.ok(monitoringService.getPricingChanges(id));
    }

    @GetMapping("/{id}/hiring-signals")
    public ResponseEntity<List<com.microsaas.competitorradar.dto.HiringSignalDto>> getHiringSignals(@PathVariable java.util.UUID id, @org.springframework.beans.factory.annotation.Autowired com.microsaas.competitorradar.service.MonitoringService monitoringService) {
        return ResponseEntity.ok(monitoringService.getHiringSignals(id));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<com.microsaas.competitorradar.dto.CustomerReviewDto>> getReviews(@PathVariable java.util.UUID id, @org.springframework.beans.factory.annotation.Autowired com.microsaas.competitorradar.service.MonitoringService monitoringService) {
        return ResponseEntity.ok(monitoringService.getReviews(id));
    }

    @GetMapping("/{id}/social")
    public ResponseEntity<List<com.microsaas.competitorradar.dto.SocialMentionDto>> getSocialMentions(@PathVariable java.util.UUID id, @org.springframework.beans.factory.annotation.Autowired com.microsaas.competitorradar.service.MonitoringService monitoringService) {
        return ResponseEntity.ok(monitoringService.getSocialMentions(id));
    }

    @GetMapping("/{id}/press")
    public ResponseEntity<List<com.microsaas.competitorradar.dto.PressMentionDto>> getPressMentions(@PathVariable java.util.UUID id, @org.springframework.beans.factory.annotation.Autowired com.microsaas.competitorradar.service.MonitoringService monitoringService) {
        return ResponseEntity.ok(monitoringService.getPressMentions(id));
    }
}
