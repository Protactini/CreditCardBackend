# 💳 CreditCardRec API

A Spring Boot RESTful API designed to support the CreditCardRec iOS application. This backend service manages credit card data and cashback categories, providing endpoints for card management and cashback filtering.

---

## 🚀 Features

- Card Management: Create, retrieve, update, and delete credit card entries.
- Cashback Categories: Associate cards with various cashback areas and percentages.
- Filtering: Retrieve cards based on selected cashback categories.
- RESTful Endpoints: JSON-based API endpoints for seamless integration.

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- H2 Database (for development and testing)
- Maven (build tool)

---

## 📦 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository:

   git clone https://github.com/yourusername/creditcardrec-api.git  
   cd creditcardrec-api

2. Build the project:

   mvn clean install

3. Run the application:

   mvn spring-boot:run

   The API will be accessible at http://localhost:8080.

---

## 📚 API Documentation

### Endpoints

- GET /api/cards: Retrieve all credit cards.
- POST /api/cards: Add a new credit card.
- GET /api/cards/{id}: Retrieve a specific credit card by ID.
- PUT /api/cards/{id}: Update an existing credit card.
- DELETE /api/cards/{id}: Delete a credit card.

### Sample Request

POST /api/cards

{
  "name": "Super Saver Card",
  "paymentMethod": "VISA",
  "cashBack": [
    {
      "area": "GROCERY_STORES",
      "percentage": 3.0
    },
    {
      "area": "GAS_STATIONS",
      "percentage": 2.0
    }
  ]
}

---

## 🧪 Testing

Run tests using Maven:

mvn test

---

## 📁 Project Structure

creditcardrec-api/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com.example.creditcardrec/
│ │ │ ├── controller/
│ │ │ ├── model/
│ │ │ ├── repository/
│ │ │ └── CreditCardRecApplication.java
│ │ └── resources/
│ │ ├── application.properties
│ │ └── data.sql
│ └── test/
│ └── java/
│ └── com.example.creditcardrec/
├── pom.xml
└── README.md

