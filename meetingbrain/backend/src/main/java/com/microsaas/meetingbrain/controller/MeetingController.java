package com.microsaas.meetingbrain.controller;

import com.microsaas.meetingbrain.model.*;
import com.microsaas.meetingbrain.service.MeetingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping("/meetings")
    public ResponseEntity<Page<Meeting>> getMeetings() {
        return ResponseEntity.ok(new PageImpl<>(meetingService.getAllMeetings()));
    }

    @PostMapping("/meetings")
    public ResponseEntity<Meeting> createMeeting(@RequestBody Meeting meeting) {
        return ResponseEntity.ok(meetingService.createMeeting(meeting));
    }

    @GetMapping("/meetings/{id}")
    public ResponseEntity<Meeting> getMeeting(@PathVariable UUID id) {
        return ResponseEntity.ok(meetingService.getMeeting(id));
    }

    @PostMapping("/meetings/{id}/analyze")
    public ResponseEntity<Void> analyzeMeeting(@PathVariable UUID id) {
        meetingService.analyzeMeeting(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/meetings/{id}/transcript")
    public ResponseEntity<List<TranscriptLine>> getTranscript(@PathVariable UUID id) {
        return ResponseEntity.ok(meetingService.getTranscript(id));
    }

    @GetMapping("/decisions")
    public ResponseEntity<List<Decision>> getDecisions() {
        return ResponseEntity.ok(meetingService.getAllDecisions());
    }

    @GetMapping("/decisions/search")
    public ResponseEntity<List<Decision>> searchDecisions(@RequestParam String query) {
        return ResponseEntity.ok(meetingService.searchDecisions(query));
    }

    @GetMapping("/action-items")
    public ResponseEntity<List<ActionItem>> getActionItems() {
        return ResponseEntity.ok(meetingService.getAllActionItems());
    }

    @PutMapping("/action-items/{id}")
    public ResponseEntity<ActionItem> updateActionItemStatus(@PathVariable UUID id, @RequestBody ActionItem item) {
        return ResponseEntity.ok(meetingService.updateActionItemStatus(id, item.getStatus()));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getProjects() {
        return ResponseEntity.ok(meetingService.getAllProjects());
    }
}
