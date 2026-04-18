package com.microsaas.workspacemanager.dto;

import java.util.UUID;

public class TeamDto {
    private String name;
    private UUID parentTeamId;

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public UUID getParentTeamId() { return this.parentTeamId; }
    public void setParentTeamId(UUID parentTeamId) { this.parentTeamId = parentTeamId; }
}
