package com.microsaas.marketsignal.dto;

import com.microsaas.marketsignal.domain.enums.SourceType;
import lombok.Data;

@Data
public class CreateSourceRequest {
    private String name;
    private SourceType sourceType;
    private String url;
}
