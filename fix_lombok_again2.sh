#!/bin/bash
pom_file="copyoptimizer/backend/pom.xml"
# The issue is that lombok is likely not running because we overwrote the spring-boot-maven-plugin which had the lombok exclude.
# Actually, the standard spring boot starter parent configures lombok correctly if it's just declared as a dependency.
# Let's restore the original pom structure and just ensure we have the dependency.
git checkout copyoptimizer/backend/pom.xml
