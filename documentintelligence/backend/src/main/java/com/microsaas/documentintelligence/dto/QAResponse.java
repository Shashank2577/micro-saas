package com.microsaas.documentintelligence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QAResponse {
    private String answer;
    private List<String> sources;
}
