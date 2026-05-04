# COMP2005 Assessment 2 – Automated Software Testing

This repository contains my COMP2005 Assessment 2 project: a Java Spring Boot web-service API connected to the provided Hospital Maternity Unit API, plus a Java Swing desktop GUI.

## Project Structure

- `app/` – Spring Boot application source code and tests
- `.github/workflows/` – GitHub Actions CI workflow

## Implemented Features

The API implements the four required business features:

- F1: List rooms used by a specific patient
- F2: List patients who have been in a specific room within the last 7 days
- F3: Identify the least used room
- F4: List staff responsible for 3 or more patients concurrently

The Swing GUI connects to the F1 endpoint and allows a user to search for rooms used by a patient.

## Requirements

- Java 25
- Gradle wrapper included
- Internet connection required for system testing because the application connects to the external Hospital Maternity Unit API

## How to Run Tests

From the `app` folder:

```bash
./gradlew test
