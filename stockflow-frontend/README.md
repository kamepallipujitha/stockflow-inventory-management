# StockFlow — Inventory Management System

A modern SaaS inventory management dashboard built with React, Vite, JavaScript, and Tailwind CSS. It talks to an existing Spring Boot REST API for all data and authentication.

## Features

- **Authentication** — Login and registration pages using JWT from the Spring Boot API. The token is stored locally and sent automatically as `Authorization: Bearer <token>` on every protected request.
- **Dashboard** — Stat cards for Total Products, Total Quantity, and Low Stock Products, plus a low-stock alerts list.
- **Product management** — Searchable, sortable product table with add, edit, and delete (with confirmation) flows.
- **Low-stock indicators** — Color-coded status badges for in-stock, low-stock, and out-of-stock items.
- **Responsive sidebar navigation** with a mobile drawer and logout.
- **Auto-logout** on expired/invalid tokens (401/403).

## Tech stack

- React 18 + Vite
- React Router (client-side routing + protected routes)
- Axios (centralized API service with JWT interceptors)
- Tailwind CSS
- lucide-react icons

## Getting started

```bash
npm install
npm run dev
```

The dev server starts on the Vite default port (usually 5173).

## Backend

All data comes from the Spring Boot API running at `http://localhost:8085`. The base URL is configured in `src/services/api.js`.

| Action            | Method & Endpoint              |
| ----------------- | ------------------------------ |
| Login             | `POST /api/auth/login`         |
| Register          | `POST /api/auth/register`      |
| List products     | `GET /api/products`            |
| Get product       | `GET /api/products/{id}`       |
| Create product    | `POST /api/products`           |
| Update product    | `PUT /api/products/{id}`       |
| Delete product    | `DELETE /api/products/{id}`    |
| Dashboard stats   | `GET /api/products/dashboard`  |

> No database is included in this project. All persistence is handled by the Spring Boot backend.

## Project structure

```
src/
  services/api.js          # Centralized Axios instance + endpoints
  context/AuthContext.jsx  # Auth state, login/register/logout
  components/
    Layout.jsx             # Sidebar + topbar shell
    Sidebar.jsx            # Responsive nav with logout
    Topbar.jsx
    Modal.jsx              # Reusable modal dialog
    Spinner.jsx
    ProtectedRoute.jsx     # Guards authenticated routes
    PublicOnlyRoute.jsx    # Redirects signed-in users away from auth pages
    AuthShell.jsx          # Shared split layout for login/register
  pages/
    Login.jsx
    Register.jsx
    Dashboard.jsx
    Products.jsx
  App.jsx                  # Routes
  main.jsx                 # Entry
```
