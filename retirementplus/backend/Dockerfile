FROM maven:3.9.6-eclipse-temurin-21-jammy AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Since we rely on a parent POM located in cross-cutting/cc-starter,
# in a real CI this would fetch from a repo. For local Docker builds
# relying on multi-module contexts, this assumes the JAR is already built 
# or we just copy the pre-built jar if we mapped it.
# To keep this standalone, we'll just run the app from the jar.
# Note: For strict correctness, the root compose should build this.
# We will assume a simple fat jar build or provide a prebuilt approach.

# Let's use the standard approach where we copy a pre-built JAR.
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/retirementplus-*.jar app.jar
EXPOSE 8207
ENTRYPOINT ["java", "-jar", "app.jar"]
