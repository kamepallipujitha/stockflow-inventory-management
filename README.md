# 📦 StockFlow – SaaS Inventory Management System

<p align="center">
  <strong>A smart, secure, and scalable inventory management solution for modern businesses.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?logo=openjdk" />
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1.2-brightgreen?logo=springboot" />
  <img src="https://img.shields.io/badge/React-Frontend-61DAFB?logo=react" />
  <img src="https://img.shields.io/badge/MySQL-Database-4479A1?logo=mysql" />
  <img src="https://img.shields.io/badge/JWT-Authentication-black?logo=jsonwebtokens" />
  <img src="https://img.shields.io/badge/Status-Development-blue" />
</p>

---

## 🚀 About the Project

**StockFlow** is a full-stack SaaS Inventory Management System designed to help businesses manage their products and monitor stock levels efficiently.

The application provides secure user authentication, organization-based data isolation, product management, and an interactive dashboard to track inventory.

It is developed using **React.js, Spring Boot, Java, and MySQL**, following a REST API-based architecture.

## ✨ Key Features

### 🔐 Authentication & Security
- User registration and login.
- JWT-based authentication.
- Password encryption using BCrypt.
- Protected API endpoints.
- Automatic token attachment to API requests.

### 🏢 Multi-Tenant Architecture
- Organization-based user management.
- Each organization has isolated inventory data.
- Users can access only their organization's products.
- Organization-specific stock threshold settings.

### 📦 Product Management
- Add new products.
- View all products.
- Update product information.
- Delete products.
- Manage product name, SKU, description, and quantity.
- Track cost price and selling price.
- Configure low-stock thresholds.
- Ensure SKU uniqueness within an organization.

### 📊 Interactive Dashboard
- Total product count.
- Total inventory quantity.
- Low-stock product overview.
- Quick inventory monitoring.

### 🔍 Search, Pagination & Sorting
- Search products by name, SKU, or description.
- Server-side pagination.
- Sort products by supported fields.
- Ascending and descending sorting.
- Efficient product retrieval using Spring Data JPA.

## 🛠️ Technology Stack

| Layer | Technologies |
|---|---|
| Frontend | React.js, JavaScript, HTML5, CSS3 |
| Styling | Tailwind CSS |
| Backend | Java 21, Spring Boot |
| API | RESTful APIs |
| Database | MySQL 8 |
| ORM | Spring Data JPA, Hibernate |
| Security | Spring Security, JWT |
| Password Encryption | BCrypt |
| HTTP Client | Axios |
| Build Tool | Maven |
| Development Tools | VS Code, Eclipse, Postman |

## 🏗️ Project Architecture

```text
StockFlow/
│
├── stockflow-backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/stockflow/stockflow_backend/
│   │       │       ├── config/
│   │       │       ├── Controller/
│   │       │       ├── dto/
│   │       │       ├── entity/
│   │       │       ├── Repository/
│   │       │       ├── security/
│   │       │       └── Service/
│   │       └── resources/
│   └── pom.xml
│
├── stockflow-frontend/
│   ├── public/
│   ├── src/
│   │   ├── api/
│   │   ├── assets/
│   │   ├── components/
│   │   ├── context/
│   │   ├── pages/
│   │   └── services/
│   ├── package.json
│   └── vite.config.js
│
└── README.md
```

## ⚙️ Installation & Setup

Follow these steps to run StockFlow locally.

### 1. Clone the Repository

```bash
git clone https://github.com/kamepallipujitha/stockflow-inventory-management.git
```

```bash
cd stockflow-inventory-management
```

### 2. Configure the MySQL Database

Start your MySQL server and create the database:

```sql
CREATE DATABASE stockflow_db;
```

Configure your database credentials in the backend configuration.

### 3. Run the Spring Boot Backend

Navigate to the backend folder:

```bash
cd stockflow-backend
```

Configure the required database and JWT properties in `src/main/resources/application.properties`.

Use environment variables for sensitive values rather than committing passwords or JWT secrets.

Then run:

```bash
mvn spring-boot:run
```

Backend URL:

```text
http://localhost:8085
```

### 4. Run the React Frontend

Open another terminal and navigate to:

```bash
cd stockflow-frontend
```

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

## 🔗 REST API Endpoints

### Authentication APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user and organization |
| POST | `/api/auth/login` | Authenticate a user |

### Product APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/products` | Create a product |
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| PUT | `/api/products/{id}` | Update a product |
| DELETE | `/api/products/{id}` | Delete a product |
| GET | `/api/products/dashboard` | Get dashboard statistics |
| GET | `/api/products/paged` | Get paginated and sorted products |
| GET | `/api/products/search` | Search products with pagination and sorting |

**Example search request:**

```text
GET /api/products/search?keyword=laptop&page=0&size=5&sortBy=name&direction=asc
```

Protected endpoints require a valid JWT token in the Authorization header:

```text
Authorization: Bearer <your-token>
```

## 🔒 Security Implementation

StockFlow uses Spring Security and JWT to secure its backend APIs.

- JWT tokens are generated after successful authentication.
- BCrypt is used for password hashing.
- Protected endpoints require authentication.
- Organization ID is used to scope inventory operations.
- Users cannot access products belonging to other organizations through the application APIs.

## 🎯 Future Enhancements

- Inventory transaction history.
- Stock movement tracking.
- Advanced analytics and reports.
- Email notifications for low-stock products.
- CSV and Excel import/export.
- Role-based access control.
- Cloud deployment.

## 👩‍💻 Author

**Pujitha**

B.Tech – Computer Science and Engineering

- GitHub: [@kamepallipujitha](https://github.com/kamepallipujitha)

---

