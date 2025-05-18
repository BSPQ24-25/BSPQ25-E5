# 🎟️ CinemaSeatBooking

The **CinemaSeatBooking** system is a full-featured backend application developed as part of the BSPQ course. It provides a robust platform for managing cinema seat reservations while supporting both user-facing and administrative functionalities.

---

## 🎯 Project Objectives

This project aims to achieve the following:

- 🕵️ **Browsing Screenings**: Allow cinema visitors to explore available movie screenings, including details like time, room, and availability.
- 🎫 **Seat Reservation**: Enable users to reserve specific seats for their chosen movie screenings in real time.
- 💳 **Payment Processing**: Simulate payment handling as part of the seat reservation workflow (mocked or stubbed for educational purposes).
- 🛠️ **Cinema Resource Management**: Provide functionality for cinema staff to manage operational data such as:
  - Adding and updating movies
  - Managing screening rooms
  - Scheduling movie screenings
- 🗃️ **Accurate Reservation Records**: Keep track of all reservations, ensuring data consistency and persistence across sessions.

---

## ⚙️ Architecture Overview

The backend follows a layered architecture:

- **Controller Layer** – Exposes RESTful APIs for client interaction
- **Service Layer** – Contains business logic and processes requests
- **Repository Layer** – Handles data persistence using Spring Data JPA
- **Security Layer** – Implements authentication and authorization using JWT and Spring Security

The system is built using **Java 17** and **Spring Boot**, and integrates with a **MySQL** database. It is fully containerized with **Docker**, making it easy to deploy in a consistent environment.

---

## 📂 Repository

You can access the full source code and documentation at the following GitHub repository:

🔗 **GitHub Repository**: [https://github.com/CinemaSeatBooking](https://github.com/BSPQ24-25/BSPQ25-E5)

---

## 🚀 Getting Started

### 🔧 Requirements

- Java 17
- Maven 3.8+
- Docker & Docker Compose
- MySQL

---

## 🔧 Technologies & Tools

The project makes use of a variety of tools and frameworks configured via `pom.xml` to support development, testing, documentation, and code quality:

### 🧰 Core Frameworks and Libraries

- **Spring Boot**: Rapid application development with embedded Tomcat and dependency injection.
- **Spring Data JPA**: Simplifies interaction with the database.
- **Spring Security**: Manages authentication and authorization.
- **Thymeleaf**: Template engine for future frontend integration.
- **MySQL**: Used as the relational database.
- **JWT (JSON Web Tokens)**: Used for secure stateless authentication.

### 📚 API Documentation

- **SpringDoc OpenAPI**: Generates interactive Swagger documentation.
  - Accessible at: `http://localhost:8080/swagger-ui/swagger-ui/index.html` when the application is running.

### 🧪 Testing Libraries

- **JUnit 5**: Main testing framework.
- **Mockito**: Used to mock dependencies and isolate unit tests.
- **JUnitPerf**: Enables performance testing.
- **Maven Surefire Plugin**: Executes unit tests located in `**/unit/**/*Test.java`.
- **Maven Failsafe Plugin**: Executes integration and performance tests from `**/integration/**/*IT.java` and `**/performance/**/*PT.java`.

### 🧪 Test Profiles

You can run specific test groups using Maven profiles:

| Profile ID       | Test Type       | Description                         |
|------------------|------------------|-------------------------------------|
| `unit`           | Unit Tests       | Located under `**/unit/**/*Test.java` |
| `integration`    | Integration Tests| Located under `**/integration/**/*IT.java` |
| `performance`    | Performance Tests| Located under `**/performance/**/*PT.java` |
| `all-tests`      | All              | Runs all the above tests together   |

Use the following commands to run tests:
```bash
mvn test -Punit             # Run unit tests
mvn verify -Pintegration    # Run integration tests
mvn verify -Pperformance    # Run performance tests
mvn verify -Pall-tests      # Run all tests
```

---

## 🛠️ Installation and Setup

Follow these steps to set up and run the project locally.

### 1. 📥 Clone the Repository

```bash
git clone https://github.com/BSPQ24-25/BSPQ25-E5
cd CinemaSeatBooking
```

### 2. 📦 Install Dependencies

Ensure you have the following installed:
- Java 17
- Maven 3.8+
- Docker & Docker Compose
- MySQL

### 3. 📂 Create the database

Create a databes with myslq named **cinema_db**

### 4. ⚙️ Compile the Project

Use Maven to build the project:
```bash
mvn clean install
```
> This will compile the code and download all necessary dependencies.

### 5. 🚀 Run the Application

#### Option A: Run with Maven

You can launch the application using the Spring Boot Maven plugin:
```bash
mvn spring-boot:run
```
> The application will start at http://localhost:8080

#### Option B: Run with Docker

To run the project in a containerized environment:
```bash
docker-compose up -d --build
```
To stop the services and remove volumes:
```bash
docker-compose down -v
```
> This sets up the full environment, including a MySQL database, automatically.

---

## 🧪 Running Tests

This project uses Maven profiles to differentiate between unit, integration, and performance tests.

| Test Type        | Command                                 |
|------------------|------------------------------------------|
| Unit             | `mvn test -Punit`                        |
| Integration      | `mvn verify -Pintegration`               |
| Performance      | `mvn verify -Pperformance`               |
| All Tests        | `mvn verify -Pall-tests`                 |

> 🔍 **JaCoCo** is configured to require a minimum **40% line coverage** per package.  
> The `controller` and `security` packages are **excluded from this rule**.

### 📊 Generate Coverage Report

```bash
mvn clean verify -Pall-tests
```

---

## 📚 Documentation and Reports

This project provides both source code documentation and test coverage reports for easier maintenance and quality assurance.

- Github Pages: [link](https://bspq24-25.github.io/BSPQ25-E5/index.html)
- Technical Documentation: [Doxygen](https://bspq24-25.github.io/BSPQ25-E5/CinemaSeatBooking/docs/index.html)
- Coverage Report: [Jacoco](https://bspq24-25.github.io/BSPQ25-E5/CinemaSeatBooking/target/site/jacoco/index.html)
- Performance Test Reports:
  - [Payment Service](https://bspq24-25.github.io/BSPQ25-E5/CinemaSeatBooking/target/reports/payment-service-perf-report.html)
  - [Reservation Service](https://bspq24-25.github.io/BSPQ25-E5/CinemaSeatBooking/target/reports/reservation-service-perf-report.html)
  - [Room Service](https://bspq24-25.github.io/BSPQ25-E5/CinemaSeatBooking/target/reports/room-service-perf-report.html)
  - [Screening Service](https://bspq24-25.github.io/BSPQ25-E5/CinemaSeatBooking/target/reports/screening-perf-report.html)
  - [Seat Service](https://bspq24-25.github.io/BSPQ25-E5/CinemaSeatBooking/target/reports/seat-service-perf-report.html)
  - [User Service](https://bspq24-25.github.io/BSPQ25-E5/CinemaSeatBooking/target/reports/user-service-perf-report.html)
