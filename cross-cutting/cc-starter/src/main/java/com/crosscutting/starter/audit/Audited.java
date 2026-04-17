package com.crosscutting.starter.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for opt-in business auditing on methods.
 * When placed on a method, the AuditAspect will capture
 * before/after state and record a business audit log entry.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audited {
    String action();
    String resource();
}
