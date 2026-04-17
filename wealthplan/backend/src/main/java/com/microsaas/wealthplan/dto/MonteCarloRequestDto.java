package com.microsaas.wealthplan.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class MonteCarloRequestDto {
    private UUID scenarioId;
}
