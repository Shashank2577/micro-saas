package com.microsaas.datagovernance.dto;

import java.util.List;

public class PiiDetectResponse {
    private List<String> piiTypes;

    public PiiDetectResponse() {}

    public PiiDetectResponse(List<String> piiTypes) {
        this.piiTypes = piiTypes;
    }

    public List<String> getPiiTypes() {
        return piiTypes;
    }

    public void setPiiTypes(List<String> piiTypes) {
        this.piiTypes = piiTypes;
    }
}
