package com.crosscutting.starter.rbac;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate controller or service methods to enforce RBAC permission checks.
 * The aspect will extract the current user and tenant from the security context
 * and verify the user holds the required permission.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {

    String resource();

    String action();
}
