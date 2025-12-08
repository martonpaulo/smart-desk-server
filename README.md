# Smart Desk

This repository (folder name: `smart-desk-server`) contains the Smart Desk Spring Boot service.

- Public project name: Smart Desk
- Repository folder: `smart-desk-server`
- Java package and group ID: `com.smartdesk`

Spring Boot application with a single health endpoint at `/health`.

Prerequisites
- Java 21 (JDK)
- Use the included Gradle wrapper (no global Gradle required)

Quick start (zsh)
```bash
./gradlew clean build
./gradlew bootRun
# then visit: http://localhost:8080/health
```
