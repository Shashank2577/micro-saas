#!/bin/bash
find compbenchmark/backend/src/test/java/com/microsaas/compbenchmark -type f -name "*.java" -exec sed -i 's/com.crosscutting.starter.tenancy.TenantContext/com.crosscutting.tenancy.TenantContext/g' {} +
find compbenchmark/backend/src/test/java/com/microsaas/compbenchmark -type f -name "*.java" -exec sed -i 's/com.crosscutting.starter.event.EventPublisher/com.crosscutting.event.EventPublisher/g' {} +
