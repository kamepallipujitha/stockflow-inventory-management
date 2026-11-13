# Project memory

## Product intent & audience

- **StockFlow**: professional SaaS Inventory Management System.
- Audience: businesses/operators managing product inventory via a Spring Boot backend.
- Core capabilities: JWT auth (login/register), dashboard analytics, product CRUD with search, low-stock tracking, logout.

## Brand personality & voice

- Modern, premium SaaS identity.
- Clean, professional, trustworthy tone suitable for operational business tooling.

## Visual design & UX direction

- Light theme: white and light-gray backgrounds with indigo/blue accent colors.
- Premium dashboard aesthetic.
- Responsive sidebar navigation; collapses into a slide-in drawer on mobile.
- Auth pages use a premium split-screen layout.
- Dashboard: three stat cards (Total Products, Total Quantity, Low Stock Products) plus a low-stock alerts list.
- Status badges by stock level: green "In stock", amber "Low", red "Out of stock".
- Products page: searchable/sortable table, modal-based Add/Edit forms, delete confirmation dialog.
- Reusable shared UI primitives (buttons, inputs, cards) for consistency.

## Decisions, constraints & rationale

- Stack: Vite + React (JavaScript) + Tailwind CSS; React Router for routing; Axios for HTTP.
- Centralized Axios service at `src/services/api.js` auto-attaches `Authorization: Bearer <token>` and signs out on token expiry.
- Auth context manages session; protected route guard gates authenticated pages.
- Backend: existing Spring Boot API at `http://localhost:8085` — no Supabase/Firebase, no separate backend, no fake data.
- Endpoints used:
  - `POST /api/auth/login`, `POST /api/auth/register`
  - `GET /api/products`, `POST /api/products`
  - `GET|PUT|DELETE /api/products/{id}`
  - `GET /api/products/dashboard`
- Production build passes cleanly; runnable locally via `npm install` + `npm run dev`.

## Open questions & assumptions

- Exact product entity fields not specified by user — inferred common fields (name, sku, quantity, price, threshold); confirm against backend DTO.
- Low-stock determination assumed to come from backend (`/api/products/dashboard` or per-product status); client-side threshold logic not defined.
- Dashboard response shape assumed to include total products, total quantity, and low-stock count/list; confirm JSON contract.
- Pagination strategy for products list not specified; currently client-side search/sort only.
- Registration flow: assumed response may or may not return JWT; confirm whether a separate login is required after register.