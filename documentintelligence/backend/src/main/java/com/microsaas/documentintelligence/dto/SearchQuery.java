package com.microsaas.documentintelligence.dto;

import lombok.Data;

@Data
public class SearchQuery {
    private String query;
    private int limit = 5;
}
