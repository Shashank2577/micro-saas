package com.microsaas.marketsignal.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateSegmentRequest {
    private String name;
    private String description;
    private List<String> keywords;
}
