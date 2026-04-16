package com.microsaas.videonarrator.model;

import lombok.Data;

@Data
public class RegisterVideoRequest {
    private String title;
    private String fileUrl;
    private Integer durationSeconds;
}
