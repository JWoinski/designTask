# Design Coding Task - Order Processing API

This project implements a simple **Order Processing API** that allows receiving and processing orders, including saving the order data in a MySQL database and sending order-related information asynchronously via Kafka and email. The application is built using the latest Java, Spring Boot, Kafka, MySQL, and Docker technologies.

## Table of Contents

1. [Technologies Used](#technologies-used)
2. [Project Structure](#project-structure)
3. [Setup and Installation](#setup-and-installation)
4. [How to Use](#how-to-use)
5. [Testing](#testing)
6. [License](#license)

## Technologies Used

### 1. **Java 21**
- This project uses **Java 21**, the latest version of the Java programming language, which provides performance improvements, enhanced language features, and greater support for modern applications.

### 2. **Spring Boot 3.2.3**
- **Spring Boot** is the framework used to create this RESTful API. It simplifies the development process by providing production-ready features and default configurations for web services, including a built-in embedded web server.
- Version 3.2.3 of Spring Boot is used, providing improvements over previous versions and support for the latest Java 21 features.

### 3. **Apache Kafka**
- **Apache Kafka** is used for asynchronous messaging in this project. Kafka is a distributed event streaming platform that helps manage high-throughput data communication between microservices.
- Kafka is used to send processed order information to various systems asynchronously. This helps decouple the components and allows for better scalability and fault tolerance.

### 4. **MySQL**
- **MySQL** is used as the relational database for storing order data. Spring Data JPA is used to interact with the MySQL database, providing an easy and robust way to manage entities and repositories.
- The `OrderRequestDTO` is mapped to an `Order` entity and saved into MySQL for persistence.

### 5. **Docker**
- **Docker** is used for containerizing the application, ensuring it runs consistently across different environments.
- The project includes a **Dockerfile** and **docker-compose.yml** to easily build and run the application inside a Docker container, along with the required MySQL database container.


### Key Components:

- **OrderController**: REST controller that exposes an endpoint for receiving orders via a POST request.
- **OrderService**: Contains the business logic for processing orders, saving them to the database, and sending emails asynchronously.
- **OrderRequestDTO**: Data Transfer Object (DTO) representing the incoming order details from the client.
- **Kafka Integration**: Kafka is used to send order details to an asynchronous messaging system.
- **MySQL Integration**: The application stores order data in a MySQL database using Spring Data JPA.

## Setup and Installation

### Prerequisites
- **Java 21** (JDK 21) installed.
- **Docker** installed on your system to run MySQL and the application inside containers.

### Step 1: Clone the Repository
Clone this project to your local machine:

```bash
git clone https://github.com/JWoinski/designCodingTask.git
cd designCodingTask
docker-compose up --build
```
### Configuration in application.properties

You can modify the configuration in the src/main/resources/application.properties file to adjust any settings (e.g., Kafka, database, or email service configurations).

### Kafka settings:
spring.kafka.consumer.bootstrap-servers=localhost:9092 <br>
spring.kafka.consumer.group-id=order-consumer-group <br>
spring.kafka.listener.concurrency=3 <br>

### MySQL settings:

spring.datasource.url=jdbc:mysql://localhost:3306/design_coding_task <br>
spring.datasource.username=root <br>
spring.datasource.password=password <br>
spring.jpa.hibernate.ddl-auto=update <br>

### Run the Application:
After the Docker containers are up and running, the application will be available on http://localhost:8080/api/orders.

To receive an order, you can make a POST request to /api/orders with a JSON payload that matches the OrderRequestDTO structure.

### Example Request Body:

```
{
  "shipmentNumber": "shipment123",
  "receiverEmail": "receiver@example.com",
  "receiverCountryCode": "PL",
  "senderCountryCode": "US",
  "statusCode": 1
}
```

### Example Response:
```
{
  "message": "Order received and processed."
}
```
## Architecture Description:
![DiagramUML.png](src/main/resources/DiagramUML.png)
### Client (Frontend):

The client sends a POST request to the API, passing order data (in JSON format).
This could be a web application, mobile app, or another system that communicates with the API.

### API (OrderController):

The API receives the data from the client and calls a method in the OrderService.
It then sends a response to the client with a message about the order processing.

### OrderService:

Responsible for processing the order.
After receiving the order data, it sends the data to a Kafka queue (via KafkaTemplate) to ensure asynchronous processing.

### Kafka (Message Broker):

Asynchronously processes the order data.
It sends messages to the appropriate consumers, such as the OrderListener, which listens to the "order-topic".
Kafka provides scalability and reliability for communication between the application's components.

### OrderListener:

Listens to the Kafka topic.
It receives order messages, deserializes them, and passes them to the database.
It is also responsible for other operations related to order processing (e.g., sending emails, handling statuses).

### MySQL (Database):

The application stores orders in a relational MySQL database.
Spring Data JPA is used to persist order data in the database.

### Mock Email Service:

Sends emails, but for testing purposes, this is a mocked service (it does not send actual messages).
It can be used for testing or simulating a real email service.

### Docker:

All components (including the application, Kafka, and MySQL) are run in Docker containers, providing easy configuration and deployment of the application across different environments (local, production).
