package com.microsaas.workspacemanager.dto;

import java.util.UUID;

public class InviteDto {
    private String email;
    private String role;
    private UUID teamId;

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return this.role; }
    public void setRole(String role) { this.role = role; }

    public UUID getTeamId() { return this.teamId; }
    public void setTeamId(UUID teamId) { this.teamId = teamId; }
}
