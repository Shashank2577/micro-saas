package com.microsaas.marketsignal.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class IngestSignalRequest {
    private String title;
    private String content;
    private UUID sourceId;
}
