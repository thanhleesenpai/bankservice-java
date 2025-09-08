# Bank-Service System (bankservice-java)

A simplified banking system built with Java and Spring Boot, featuring account, card, and balance management, plus a payment & notification microservice architecture. Designed as a student project, it demonstrates best practices in layering, integration, caching, messaging, security, Dockerization, and testing—clear and robust enough for a company evaluation.

---

##  Architecture Overview

Bank-Service API (Spring Boot, Account/Layers) --> Payment-Service (Spring Boot, MQ Producer) --> ActiveMQ --> Notification Service (Spring Boot)


Redis is used to cache account details and balances, reducing database load. Docker Compose orchestrates all components including Redis and ActiveMQ.

---

##  Main Features

### Bank-Service
- **Account Management**  
  - Create, read, update, delete accounts  
  - Only deletable if no linked cards and zero balance

- **Card Management**  
  - List, create, delete cards  
  - Validations: only active cards, no cards if account not found

- **Balance Management**  
  - Retrieve balance  
  - Deposit money  
  - Withdraw money only if sufficient available balance

- **Security**  
  - JWT-based authentication; account ID is extracted from the token

- **Caching (Redis)**  
  - Cache account and balance data with 10-minute expiry  
  - Cache invalidated or updated after any change

- **Integration**  
  - Payment-Service publishes to ActiveMQ  
  - Notification-Service subscribes and logs confirmation messages

- **Dockerized** (with Docker Compose)  
  - Includes Bank-Service, Payment-Service, Notification-Service, Redis, and ActiveMQ

- **Testing**  
  - Unit tests for each API (at least two test cases per endpoint)

---

##  Technologies Used

| Layer/Feature        | Technologies                        |
|----------------------|-------------------------------------|
| Framework            | Spring Boot, Spring Security, Spring Cache (Redis) |
| Messaging            | ActiveMQ (JMS)                      |
| Security             | JSON Web Token (JWT)                |
| Caching              | Redis, TTL set                      |
| Containerization     | Docker, Docker Compose              |
| Testing              | JUnit, Mockito                      |
| API Testing          | Postman                             |

---

##  Setup & Running the Project

### Prerequisites
- Java 21 and Maven
- Docker & Docker Compose installed

### Steps

1. **Clone the repository**  
   ```bash
   git clone https://github.com/thanhleesenpai/bankservice-java.git
   cd bankservice-java
   ```
2. **Start all services**

    ```bash
    docker-compose up --build
    ```
3. **Environment and Config**
Each service’s application.yaml may have placeholders for DB connections, JWT secrets, etc. Default values used if not overridden.

4. **Running Locally (without Docker)**
For each service folder:

```bash
cd bank-service
mvn spring-boot:run
```
(Similarly for payment-service and notification-service.)

API Documentation & Testing
- Detailed API endpoints are documented in each service's controllers files.

- Postman Collection: (Assuming you exported it)

    - Contains successful and failure cases for each endpoint.

    - Import it into Postman to test:

        - Create account → Success / Failure

        - Create card → Success / Failure

        - Deposit / Withdraw → Success / Insufficient balance

        - Payment → triggers notification log via ActiveMQ

Testing
Run unit tests using:

```bash
mvn test
```
There are at least two test cases per API:

- Positive scenario (e.g., successful account creation)

- Negative scenario (e.g., failing validation, insufficient balance)
