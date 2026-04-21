package com.microsaas.customersuccessos.controller;

import com.microsaas.customersuccessos.model.QbrDeck;
import com.microsaas.customersuccessos.service.QbrGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/qbr")
@RequiredArgsConstructor
public class QbrController {

    private final QbrGenerationService qbrService;

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<QbrDeck>> getQbrDecks(@PathVariable UUID accountId) {
        return ResponseEntity.ok(qbrService.getQbrDecks(accountId));
    }

    @PostMapping("/account/{accountId}")
    public ResponseEntity<QbrDeck> generateQbr(@PathVariable UUID accountId) {
        return ResponseEntity.ok(qbrService.generateQbr(accountId));
    }
}
