package com.microsaas.videonarrator.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class NarrationRequest {
    private String voiceProvider;
    private String voiceId;
    private UUID transcriptionId;
}
