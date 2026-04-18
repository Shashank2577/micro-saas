package com.microsaas.voiceagentbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiSimulationResponse {
    private String reply;
    private String intentTriggered;
}
