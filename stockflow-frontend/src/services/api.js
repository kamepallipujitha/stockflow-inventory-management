import axios from "axios";

const BASE_URL = "http://localhost:8085";

const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Attach JWT token to every request automatically
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("stockflow_token");

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    console.log("Authorization Debug:", {
      tokenExists: !!token,
      tokenLength: token?.length,
      tokenAlreadyHasBearer: token?.startsWith("Bearer "),
      headerExists: !!config.headers.Authorization,
      headerStartsCorrectly:
        config.headers.Authorization?.startsWith("Bearer "),
      headerLength: config.headers.Authorization?.length
    });

    return config;
  },
  (error) => Promise.reject(error)
);

// Auto-logout on 401/403 responses
// Auto-logout only on 401 Unauthorized
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("stockflow_token");
      localStorage.removeItem("stockflow_user");

      if (window.location.pathname !== "/login") {
        window.location.href = "/login";
      }
    }

    return Promise.reject(error);
  }
);

// ---- Auth ----
export const login = (credentials) => api.post("/api/auth/login", credentials);
export const register = (payload) => api.post("/api/auth/register", payload);

// ---- Products ----
export const getProducts = () => api.get("/api/products");
// Get products with pagination and sorting
export const getPagedProducts = ({
  page = 0,
  size = 5,
  sortBy = "name",
  direction = "asc",
}) =>
  api.get("/api/products/paged", {
    params: {
      page,
      size,
      sortBy,
      direction,
    },
  });

// Search products with pagination and sorting
export const searchProducts = ({
  keyword = "",
  page = 0,
  size = 5,
  sortBy = "name",
  direction = "asc",
}) =>
  api.get("/api/products/search", {
    params: { keyword, page, size, sortBy, direction },
  });

export const getProduct = (id) => api.get(`/api/products/${id}`);
export const createProduct = (product) => api.post("/api/products", product);
export const updateProduct = (id, product) => api.put(`/api/products/${id}`, product);
export const deleteProduct = (id) => api.delete(`/api/products/${id}`);

// ---- Dashboard ----
export const getDashboard = () => api.get("/api/products/dashboard");

export default api;
