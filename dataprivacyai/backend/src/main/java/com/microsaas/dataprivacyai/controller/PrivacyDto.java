package com.microsaas.dataprivacyai.controller;

import com.microsaas.dataprivacyai.domain.DataCategory;
import com.microsaas.dataprivacyai.domain.LegalBasis;
import com.microsaas.dataprivacyai.domain.RequestType;

public class PrivacyDto {

    public static class DataFlowRequest {
        public String sourceSystem;
        public String destinationSystem;
        public DataCategory dataCategory;
        public String transferMechanism;
        public LegalBasis legalBasis;
    }

    public static class SubjectRequest {
        public RequestType requestType;
        public String subjectEmail;
    }
}
