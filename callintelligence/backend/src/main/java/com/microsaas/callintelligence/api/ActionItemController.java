package com.microsaas.callintelligence.api;

import com.microsaas.callintelligence.domain.insight.ActionItem;
import com.microsaas.callintelligence.service.ActionItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/action-items")
public class ActionItemController {

    private final ActionItemService actionItemService;

    public ActionItemController(ActionItemService actionItemService) {
        this.actionItemService = actionItemService;
    }

    @GetMapping
    public ResponseEntity<List<ActionItem>> getAllActionItems() {
        return ResponseEntity.ok(actionItemService.getAllActionItems());
    }

    @GetMapping("/call/{callId}")
    public ResponseEntity<List<ActionItem>> getActionItemsByCall(@PathVariable UUID callId) {
        return ResponseEntity.ok(actionItemService.getActionItemsForCall(callId));
    }
}
