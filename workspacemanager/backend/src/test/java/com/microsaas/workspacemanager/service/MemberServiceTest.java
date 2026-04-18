package com.microsaas.workspacemanager.service;

import com.microsaas.workspacemanager.domain.WorkspaceMember;
import com.microsaas.workspacemanager.domain.Invitation;
import com.microsaas.workspacemanager.dto.InviteDto;
import com.microsaas.workspacemanager.dto.BulkImportDto;
import com.microsaas.workspacemanager.repository.WorkspaceMemberRepository;
import com.microsaas.workspacemanager.repository.InvitationRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private WorkspaceMemberRepository memberRepository;
    
    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private MemberService memberService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testInviteMember() {
        InviteDto dto = new InviteDto();
        dto.setEmail("test@acme.com");
        dto.setRole("MEMBER");
        
        Invitation invitation = new Invitation();
        invitation.setId(UUID.randomUUID());
        invitation.setTenantId(tenantId);
        invitation.setEmail("test@acme.com");
        
        when(invitationRepository.save(any())).thenReturn(invitation);
        doNothing().when(auditService).logAction(any(), any(), any(), any(), any());

        Invitation result = memberService.inviteMember(dto);

        assertNotNull(result);
        assertEquals("test@acme.com", result.getEmail());
        verify(invitationRepository, times(1)).save(any());
    }

    @Test
    void testAcceptInvite() {
        Invitation invitation = new Invitation();
        invitation.setId(UUID.randomUUID());
        invitation.setTenantId(tenantId);
        invitation.setEmail("test@acme.com");
        invitation.setRole("MEMBER");
        invitation.setToken("valid-token");
        invitation.setExpiresAt(LocalDateTime.now().plusDays(1));
        
        WorkspaceMember member = new WorkspaceMember();
        member.setId(UUID.randomUUID());
        member.setTenantId(tenantId);
        member.setEmail("test@acme.com");
        
        when(invitationRepository.findByToken("valid-token")).thenReturn(Optional.of(invitation));
        when(memberRepository.save(any())).thenReturn(member);
        doNothing().when(auditService).logAction(any(), any(), any(), any(), any());

        WorkspaceMember result = memberService.acceptInvite("valid-token");

        assertNotNull(result);
        assertEquals("test@acme.com", result.getEmail());
        verify(memberRepository, times(1)).save(any());
        verify(invitationRepository, times(1)).save(any());
    }
}
