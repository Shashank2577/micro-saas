package com.microsaas.workspacemanager.controller;

import com.microsaas.workspacemanager.domain.WorkspaceMember;
import com.microsaas.workspacemanager.domain.Invitation;
import com.microsaas.workspacemanager.dto.InviteDto;
import com.microsaas.workspacemanager.dto.BulkImportDto;
import com.microsaas.workspacemanager.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<WorkspaceMember>> getMembers() {
        return ResponseEntity.ok(memberService.getMembers());
    }

    @PostMapping("/invite")
    public ResponseEntity<Invitation> inviteMember(@RequestBody InviteDto dto) {
        return ResponseEntity.ok(memberService.inviteMember(dto));
    }

    @PostMapping("/invite/accept")
    public ResponseEntity<WorkspaceMember> acceptInvite(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(memberService.acceptInvite(body.get("token")));
    }

    @PostMapping("/bulk-import")
    public ResponseEntity<List<WorkspaceMember>> bulkImport(@RequestBody BulkImportDto dto) {
        return ResponseEntity.ok(memberService.bulkImport(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeMember(@PathVariable UUID id) {
        memberService.removeMember(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/bulk-update")
    public ResponseEntity<Void> bulkUpdateRoles(@RequestBody Map<String, Object> payload) {
        List<String> idsStr = (List<String>) payload.get("memberIds");
        List<UUID> memberIds = idsStr.stream().map(UUID::fromString).toList();
        String role = (String) payload.get("role");
        memberService.bulkUpdateRoles(memberIds, role);
        return ResponseEntity.ok().build();
    }
}
