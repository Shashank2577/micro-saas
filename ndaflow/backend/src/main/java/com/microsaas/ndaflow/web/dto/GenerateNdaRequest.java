package com.microsaas.ndaflow.web.dto;

import com.microsaas.ndaflow.domain.NdaType;
import lombok.Data;

@Data
public class GenerateNdaRequest {
    private String title;
    private String counterparty;
    private NdaType ndaType;
}
