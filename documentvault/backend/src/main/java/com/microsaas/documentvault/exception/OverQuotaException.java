package com.microsaas.documentvault.exception;

public class OverQuotaException extends RuntimeException {
    public OverQuotaException(String message) {
        super(message);
    }
}
