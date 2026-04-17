#!/bin/bash
for f in $(find socialintelligence/backend/src/main/java/com/crosscutting/socialintelligence -name "*.java"); do
    sed -i 's/^import lombok.Data;//g' $f
    sed -i 's/^import lombok.NoArgsConstructor;//g' $f
    sed -i 's/^import lombok.AllArgsConstructor;//g' $f
    sed -i 's/^import lombok.Builder;//g' $f
    sed -i '/^package com.crosscutting/a import lombok.Data;\nimport lombok.NoArgsConstructor;\nimport lombok.AllArgsConstructor;\nimport lombok.Builder;' $f
done
