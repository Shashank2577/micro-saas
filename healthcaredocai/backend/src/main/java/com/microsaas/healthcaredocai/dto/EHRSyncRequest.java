package com.microsaas.healthcaredocai.dto;

import java.util.UUID;

public class EHRSyncRequest {
    private UUID noteId;
    private String ehrSystem;

    public UUID getNoteId() { return noteId; }
    public void setNoteId(UUID noteId) { this.noteId = noteId; }
    public String getEhrSystem() { return ehrSystem; }
    public void setEhrSystem(String ehrSystem) { this.ehrSystem = ehrSystem; }
}
