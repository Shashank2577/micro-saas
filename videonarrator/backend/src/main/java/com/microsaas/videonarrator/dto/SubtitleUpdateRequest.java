package com.microsaas.videonarrator.dto;

import lombok.Data;

@Data
public class SubtitleUpdateRequest {
    private Long startTimeMs;
    private Long endTimeMs;
    private String content;
}
