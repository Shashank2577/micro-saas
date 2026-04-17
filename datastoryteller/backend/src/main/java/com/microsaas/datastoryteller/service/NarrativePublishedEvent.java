package com.microsaas.datastoryteller.service;

import com.microsaas.datastoryteller.domain.model.NarrativeReport;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NarrativePublishedEvent extends ApplicationEvent {
    private final NarrativeReport report;

    public NarrativePublishedEvent(Object source, NarrativeReport report) {
        super(source);
        this.report = report;
    }
}
