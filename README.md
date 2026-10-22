# 🌱 Smart Agriculture Marketplace

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.3.1-blue.svg)](https://reactjs.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-blue.svg)](https://www.mysql.com/)

A modern full-stack web application designed to connect local farmers directly with consumers. **Smart Agriculture Marketplace** empowers farmers to list produce with real-time stock management and transparent pricing while enabling customers to discover fresh crops, place orders with automatic inventory deduction, and track order histories.

---

## 🌟 Key Features

### 🚜 For Farmers
- **Account Registration & Authentication**: Secure sign-up and login with BCrypt encrypted passwords.
- **Produce Management**: Add, update, and manage crops with categories (*Vegetables, Fruits, Grains, Pulses, Dairy, etc.*), quantity, unit (*kg, quintal, bag, piece, litre*), and price.
- **Farmer Dashboard**: Real-time listing statistics, active in-stock produce counts, and individual farmer performance monitoring.

### 🛒 For Customers
- **Produce Marketplace**: Real-time product discovery with live stock count and farmer attribution.
- **Live Search**: Instant keyword-based crop search.
- **Order Placement**: Place orders with automatic inventory validation and real-time stock deduction.
- **Customer Dashboard**: Overview of order history, pending orders count, and total expenditure.

---

## 🏗️ Tech Stack & Architecture

- **Backend**: Java 17+, Spring Boot 3.4.5, Spring Data JPA, Hibernate, Spring Web, Spring Security Crypto (`BCryptPasswordEncoder`).
- **Database**: MySQL (`agriculture_marketplace`).
- **Frontend**: React 18, Axios, Responsive Modern CSS with Google Fonts (*Manrope* & *DM Sans*).
- **Architecture**: RESTful micro-architecture with MVC layers, global exception handling, and transactional inventory management.

---

## 📂 Project Structure

```
smart-agriculture-marketplace/
├── backend/
│   ├── pom.xml                                  # Maven dependencies
│   └── src/main/
│       ├── resources/
│       │   └── application.properties           # Server port (9998) & MySQL credentials
│       └── java/com/agriculture/marketplace/
│           ├── AgricultureMarketplaceApplication.java
│           ├── config/
│           │   ├── CorsConfig.java              # CORS mapped for React frontend
│           │   └── GlobalExceptionHandler.java  # REST exception handler (400, 404, 409, 500)
│           ├── controller/
│           │   ├── AuthController.java          # User registration & authentication
│           │   ├── ProductController.java       # Product CRUD & search endpoints
│           │   ├── OrderController.java         # Order creation & retrieval
│           │   └── DashboardController.java     # Farmer & customer analytics
│           ├── model/
│           │   ├── User.java                    # User entity (FARMER / CUSTOMER)
│           │   ├── Product.java                 # Produce entity linked to Farmer
│           │   ├── CustomerOrder.java           # Order entity
│           │   └── OrderItem.java               # Purchased line item snapshot
│           ├── repository/                      # Spring Data JPA Repositories
│           └── service/                         # Business & Transactional Service Layer
├── frontend/
│   ├── package.json                             # React dependencies
│   ├── public/
│   │   └── index.html
│   └── src/
│       ├── App.js                               # Application state & views
│       ├── styles.css                           # Agricultural theme responsive design
│       └── services/
│           └── api.js                           # Axios configuration
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites
- **Java 17+** (Java 17, 21, or 25 LTS)
- **Node.js** (v18+ LTS recommended) & npm
- **MySQL Server** (running on port `3306`)

---

### 1. Database Setup
Log into your MySQL console or MySQL Workbench and run:
```sql
CREATE DATABASE agriculture_marketplace;
```

---

### 2. Backend Setup
1. Open `backend/src/main/resources/application.properties` and verify your MySQL username and password:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/agriculture_marketplace
   spring.datasource.username=root
   spring.datasource.password=root
   ```
2. Open the `backend` folder in your IDE (IntelliJ IDEA / Eclipse / Spring Tool Suite) as a Maven project.
3. Run `AgricultureMarketplaceApplication.java`.
4. The backend will start on **`http://localhost:9998`**.

---

### 3. Frontend Setup
1. Open a terminal and navigate to the `frontend` folder:
   ```bash
   cd frontend
   npm install
   ```
2. Start the development server:
   ```bash
   npm start
   ```
3. Open **`http://localhost:3000`** in your browser.

---

## 📡 API Documentation

### 🔐 Authentication (`/api/auth`)
| Method | Endpoint | Description | Request Body |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register new user | `{ "fullName": "...", "email": "...", "password": "...", "phone": "...", "address": "...", "role": "FARMER" \| "CUSTOMER" }` |
| `POST` | `/api/auth/login` | Authenticate user | `{ "email": "...", "password": "..." }` |

### 🌾 Products (`/api/products`)
| Method | Endpoint | Description | Request Body / Query |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/products` | Get all produce | Query param: `?search=<keyword>` (optional) |
| `GET` | `/api/products/{id}` | Get produce by ID | - |
| `POST` | `/api/products` | Create produce listing | `{ "productName": "...", "category": "...", "price": 40.0, "quantity": 100.0, "unit": "kg", "farmer": { "userId": 1 } }` |
| `PUT` | `/api/products/{id}` | Update produce | Full `Product` object |
| `DELETE` | `/api/products/{id}` | Delete produce | - |

### 📦 Orders (`/api/orders`)
| Method | Endpoint | Description | Request Body |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/orders` | Place new order | `{ "customerId": 2, "items": [{ "productId": 1, "quantity": 5 }] }` |
| `GET` | `/api/orders/customer/{customerId}` | Get customer order history | - |

### 📊 Dashboard (`/api/dashboard`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/dashboard/farmer/{id}` | Retrieves listing stats and active stock counts for a farmer |
| `GET` | `/api/dashboard/customer/{id}` | Retrieves total orders, pending orders, and total spent for a customer |

---

## 👤 Author

Developed by **[PEDDAPUNDRA KOTI REDDY](https://github.com/kotireddypeddapundra-netizen)**.
