#!/bin/bash
for f in onboardflow/backend/src/test/java/com/microsaas/onboardflow/controller/*Test.java; do
  sed -i 's/\.header("X-Tenant-ID", tenantId.toString())/\.header("X-Tenant-ID", tenantId.toString()).with(csrf())/g' "$f"
done
