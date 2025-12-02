# Spring Boot to Quarkus Migration Example

This module demonstrates a small **Spring Boot 2.7.18** application that uses deprecated APIs and patterns requiring migration to **Quarkus 3.28.2**.

It is a simplified version of [MTA getting started - Spring Boot to Quarkus](https://github.com/migtools/getting-started/tree/main/springboot-to-quarkus-migration).

## Build and run

```bash
mvn clean package
```

```
java -jar target/springboot-to-quarkus-migration-1.0.0-SNAPSHOT.jar
```

## Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=TodoControllerTest

mvn test jacoco:report
```

## Migration Analysis

To analyze this application with MTA/Konveyor:

```bash
# Using Konveyor CLI (when available)
konveyor analyze --input springboot-to-quarkus-migration/ --target quarkus3 --output reports/

# Using MTA CLI
mta-cli --input springboot-to-quarkus-migration/ --target quarkus3 --output reports/springboot-quarkus-analysis
```