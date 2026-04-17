package com.microsaas.healthcaredocai.dto;

import com.microsaas.healthcaredocai.domain.NoteType;
import java.util.UUID;

public class GenerateNoteRequest {
    private UUID encounterId;
    private NoteType noteType;
    private String specialty;

    public UUID getEncounterId() { return encounterId; }
    public void setEncounterId(UUID encounterId) { this.encounterId = encounterId; }
    public NoteType getNoteType() { return noteType; }
    public void setNoteType(NoteType noteType) { this.noteType = noteType; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}
