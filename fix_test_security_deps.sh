#!/bin/bash
# Adding spring-security-test dependency to pom.xml
sed -i '/<\/dependencies>/i \
    <dependency>\
        <groupId>org.springframework.security</groupId>\
        <artifactId>spring-security-test</artifactId>\
        <scope>test</scope>\
    </dependency>' onboardflow/backend/pom.xml
