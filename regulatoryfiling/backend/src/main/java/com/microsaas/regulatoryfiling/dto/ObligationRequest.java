package com.microsaas.regulatoryfiling.dto;

import com.microsaas.regulatoryfiling.domain.RecurrencePattern;

import java.time.LocalDate;

public class ObligationRequest {
    private String name;
    private String jurisdiction;
    private String filingType;
    private LocalDate dueDate;
    private RecurrencePattern recurrencePattern;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getJurisdiction() { return jurisdiction; }
    public void setJurisdiction(String jurisdiction) { this.jurisdiction = jurisdiction; }

    public String getFilingType() { return filingType; }
    public void setFilingType(String filingType) { this.filingType = filingType; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public RecurrencePattern getRecurrencePattern() { return recurrencePattern; }
    public void setRecurrencePattern(RecurrencePattern recurrencePattern) { this.recurrencePattern = recurrencePattern; }
}
