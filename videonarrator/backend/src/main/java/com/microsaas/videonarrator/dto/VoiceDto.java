package com.microsaas.videonarrator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoiceDto {
    private String id;
    private String provider;
    private String name;
}
