# Spring Boot to Quarkus Migration Example

This module demonstrates a small **Spring Boot 2.7.18** application that uses deprecated APIs and patterns requiring migration to **Quarkus 3.28.2**.

It is a simplified version of [MTA getting started - Spring Boot to Quarkus](https://github.com/migtools/getting-started/tree/main/springboot-to-quarkus-migration).

## Build and run

Build
```bash
mvn clean package
```

Run
```
java -jar target/springboot-to-quarkus-migration-1.0.0-SNAPSHOT.jar
```

## Running Tests

All tests
```bash
mvn test
```

Specific test class
```bash
mvn test -Dtest=TodoControllerTest
```

## Migration Analysis

To analyze this application with MTA/Konveyor:

Using Konveyor CLI (when available)
```bash
konveyor analyze --input springboot-to-quarkus-migration/ --target quarkus3 --output reports/
```

Using MTA CLI
```bash
mta-cli --input springboot-to-quarkus-migration/ --target quarkus3 --output reports/springboot-quarkus-analysis
```