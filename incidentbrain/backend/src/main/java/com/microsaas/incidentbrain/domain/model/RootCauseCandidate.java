package com.microsaas.incidentbrain.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RootCauseCandidate {
    private String description;
    private Double confidence;
    private List<String> evidence;
    private String aiReasoning;
}
