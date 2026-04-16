package com.crosscutting.starter.audit;

import com.crosscutting.starter.tenancy.TenantContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditAspectTest {

    @Mock
    private BusinessAuditService businessAuditService;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Audited audited;

    @Mock
    private Signature signature;

    private AuditAspect aspect;

    @BeforeEach
    void setUp() {
        aspect = new AuditAspect(businessAuditService);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void callsAuditServiceWithCorrectActionAndResource() throws Throwable {
        UUID resourceId = UUID.randomUUID();
        Object inputArg = resourceId;
        Object returnValue = "createdEntity";

        when(audited.action()).thenReturn("CREATE");
        when(audited.resource()).thenReturn("Order");
        when(joinPoint.getArgs()).thenReturn(new Object[]{inputArg});
        when(joinPoint.proceed()).thenReturn(returnValue);

        aspect.auditMethod(joinPoint, audited);

        verify(businessAuditService).record(
                isNull(),          // tenantId (no TenantContext set)
                isNull(),          // userId (no SecurityContext)
                eq("CREATE"),
                eq("Order"),
                eq(resourceId),    // extracted from UUID arg
                eq(inputArg),      // beforeState = first arg
                eq(returnValue)    // afterState = return value
        );
    }

    @Test
    void proceedsWithTheMethod() throws Throwable {
        String expectedResult = "result";

        when(audited.action()).thenReturn("READ");
        when(audited.resource()).thenReturn("Item");
        when(joinPoint.getArgs()).thenReturn(new Object[]{});
        when(joinPoint.proceed()).thenReturn(expectedResult);

        Object result = aspect.auditMethod(joinPoint, audited);

        assertEquals(expectedResult, result);
        verify(joinPoint).proceed();
    }

    @Test
    void handlesExceptionFromAuditedMethod() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{});
        when(joinPoint.proceed()).thenThrow(new RuntimeException("method failed"));

        assertThrows(RuntimeException.class, () -> aspect.auditMethod(joinPoint, audited));

        // Audit should NOT be recorded since the method threw
        verify(businessAuditService, never()).record(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void handlesExceptionFromAuditServiceGracefully() throws Throwable {
        when(audited.action()).thenReturn("UPDATE");
        when(audited.resource()).thenReturn("Item");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"data"});
        when(joinPoint.proceed()).thenReturn("updated");
        when(joinPoint.getSignature()).thenReturn(signature);

        doThrow(new RuntimeException("audit DB error"))
                .when(businessAuditService).record(any(), any(), any(), any(), any(), any(), any());

        // Should not throw - audit failure is logged but swallowed
        Object result = aspect.auditMethod(joinPoint, audited);
        assertEquals("updated", result);
    }

    @Test
    void extractsResourceIdFromUuidArgument() throws Throwable {
        UUID resourceId = UUID.randomUUID();
        String otherArg = "someString";

        when(audited.action()).thenReturn("DELETE");
        when(audited.resource()).thenReturn("Order");
        when(joinPoint.getArgs()).thenReturn(new Object[]{otherArg, resourceId});
        when(joinPoint.proceed()).thenReturn(null);

        aspect.auditMethod(joinPoint, audited);

        verify(businessAuditService).record(
                isNull(), isNull(),
                eq("DELETE"), eq("Order"),
                eq(resourceId),     // UUID extracted from args
                eq(otherArg),       // beforeState = first arg
                isNull()            // afterState = null return
        );
    }

    @Test
    void usesTenantContextWhenAvailable() throws Throwable {
        UUID tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);

        when(audited.action()).thenReturn("CREATE");
        when(audited.resource()).thenReturn("Item");
        when(joinPoint.getArgs()).thenReturn(new Object[]{});
        when(joinPoint.proceed()).thenReturn("result");

        aspect.auditMethod(joinPoint, audited);

        verify(businessAuditService).record(
                eq(tenantId), isNull(),
                eq("CREATE"), eq("Item"),
                isNull(), isNull(), eq("result")
        );
    }
}
