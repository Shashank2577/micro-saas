#!/bin/bash
find compbenchmark/backend/src/main/java/com/microsaas/compbenchmark/services -type f -name "*.java" -exec sed -i 's/com.crosscutting.tenancy.TenantContext/com.crosscutting.starter.tenancy.TenantContext/g' {} +
find compbenchmark/backend/src/main/java/com/microsaas/compbenchmark/services -type f -name "*.java" -exec sed -i 's/com.crosscutting.event.EventPublisher/com.crosscutting.starter.event.EventPublisher/g' {} +
