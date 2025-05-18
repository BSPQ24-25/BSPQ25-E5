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
- MySQL (automatically configured via Docker)

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
