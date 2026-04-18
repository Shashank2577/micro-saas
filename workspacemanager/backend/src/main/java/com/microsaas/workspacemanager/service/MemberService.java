package com.microsaas.workspacemanager.service;

import com.microsaas.workspacemanager.domain.WorkspaceMember;
import com.microsaas.workspacemanager.domain.Invitation;
import com.microsaas.workspacemanager.dto.InviteDto;
import com.microsaas.workspacemanager.dto.BulkImportDto;
import com.microsaas.workspacemanager.repository.WorkspaceMemberRepository;
import com.microsaas.workspacemanager.repository.InvitationRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final WorkspaceMemberRepository memberRepository;
    private final InvitationRepository invitationRepository;
    private final AuditService auditService;

    public List<WorkspaceMember> getMembers() {
        return memberRepository.findByTenantId(TenantContext.require());
    }

    public Invitation inviteMember(InviteDto dto) {
        UUID tenantId = TenantContext.require();
        Invitation invitation = new Invitation();
        invitation.setTenantId(tenantId);
        invitation.setEmail(dto.getEmail());
        invitation.setRole(dto.getRole());
        invitation.setTeamId(dto.getTeamId());
        invitation.setToken(UUID.randomUUID().toString());
        invitation.setExpiresAt(LocalDateTime.now().plusDays(30));
        invitation.setStatus("PENDING");
        invitation = invitationRepository.save(invitation);

        auditService.logAction(tenantId, "MEMBER_INVITED", null, invitation.getId(), "{\"email\":\"" + dto.getEmail() + "\"}");
        return invitation;
    }

    public WorkspaceMember acceptInvite(String token) {
        Invitation invitation = invitationRepository.findByToken(token).orElseThrow();
        if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invitation expired");
        }
        
        WorkspaceMember member = new WorkspaceMember();
        member.setTenantId(invitation.getTenantId());
        member.setUserId(UUID.randomUUID()); // In reality this comes from auth context
        member.setEmail(invitation.getEmail());
        member.setName(invitation.getEmail().split("@")[0]);
        member.setRole(invitation.getRole());
        member.setStatus("ACTIVE");
        member = memberRepository.save(member);

        invitation.setStatus("ACCEPTED");
        invitationRepository.save(invitation);

        auditService.logAction(member.getTenantId(), "INVITE_ACCEPTED", member.getUserId(), member.getId(), "{}");
        return member;
    }

    public List<WorkspaceMember> bulkImport(BulkImportDto dto) {
        UUID tenantId = TenantContext.require();
        List<WorkspaceMember> members = dto.getRows().stream().map(row -> {
            WorkspaceMember member = new WorkspaceMember();
            member.setTenantId(tenantId);
            member.setUserId(UUID.randomUUID());
            member.setEmail(row.getEmail());
            member.setName(row.getName());
            member.setRole(row.getRole());
            member.setStatus("ACTIVE");
            return member;
        }).collect(Collectors.toList());

        members = memberRepository.saveAll(members);
        auditService.logAction(tenantId, "BULK_IMPORT", null, null, "{\"count\":" + members.size() + "}");
        return members;
    }

    public void removeMember(UUID memberId) {
        WorkspaceMember member = memberRepository.findById(memberId).orElseThrow();
        member.setStatus("DEPROVISIONED");
        memberRepository.save(member);
        
        auditService.logAction(member.getTenantId(), "MEMBER_REMOVED", null, member.getId(), "{}");
    }

    public void bulkUpdateRoles(List<UUID> memberIds, String role) {
        UUID tenantId = TenantContext.require();
        List<WorkspaceMember> members = memberRepository.findAllById(memberIds);
        members.forEach(member -> member.setRole(role));
        memberRepository.saveAll(members);

        auditService.logAction(tenantId, "BULK_ROLE_UPDATE", null, null, "{\"role\":\"" + role + "\", \"count\":" + members.size() + "}");
    }
}
