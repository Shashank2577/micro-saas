#!/bin/bash
find compbenchmark/backend/src/main/java/com/microsaas/compbenchmark/services -type f -name "*.java" -exec sed -i 's/com.crosscutting.starter.tenancy.TenantContext/com.crosscutting.tenancy.TenantContext/g' {} +
find compbenchmark/backend/src/main/java/com/microsaas/compbenchmark/services -type f -name "*.java" -exec sed -i 's/com.crosscutting.starter.event.EventPublisher/com.crosscutting.event.EventPublisher/g' {} +
