#!/bin/bash
for file in financenarrator/backend/src/main/java/com/microsaas/financenarrator/service/*Service.java; do
    if [[ $file != *"AiOrchestration"* && $file != *"EventPublisher"* ]]; then
        sed -i 's/T result/var result/g' $file
    fi
done
