package com.microsaas.ndaflow.web.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ProposeRedlineRequest {
    private UUID clauseId;
    private String proposedText;
    private String rationale;
}
