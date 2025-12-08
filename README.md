# smartdesk-server

Minimal Spring Boot service (Java 21). Contains a small health endpoint.

Prerequisites
- Java 21 (JDK) installed
- No global Gradle required — use the included Gradle wrapper

Quick start (zsh)
1. Build:
   ./gradlew clean build

2. Run:
   ./gradlew bootRun
   # or run the built jar:
   # ./gradlew bootJar
   # java -jar build/libs/<artifact>.jar

3. Test:
   ./gradlew test

Notes
- This repository intentionally ignores local build artifacts (`build/`, `generated/`, `.gradle/`).
- Keep IDE-specific files out of version control; they are in `.gitignore`.

