import { createContext, useContext, useEffect, useMemo, useState } from "react";
import { login as apiLogin, register as apiRegister } from "../services/api";

const AuthContext = createContext(null);

const STORAGE_TOKEN = "stockflow_token";
const STORAGE_USER = "stockflow_user";

function readStoredUser() {
  try {
    const raw = localStorage.getItem(STORAGE_USER);
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem(STORAGE_TOKEN));
  const [user, setUser] = useState(() => readStoredUser());
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (token) localStorage.setItem(STORAGE_TOKEN, token);
    else localStorage.removeItem(STORAGE_TOKEN);
  }, [token]);

  useEffect(() => {
    if (user) localStorage.setItem(STORAGE_USER, JSON.stringify(user));
    else localStorage.removeItem(STORAGE_USER);
  }, [user]);

  const login = async ({ email, password }) => {
    setLoading(true);
    try {
      const res = await apiLogin({ email, password });
      const data = res.data;

      // Spring Boot login typically returns { token, ... } — handle common shapes.
      const jwt =
        data.token ||
        data.accessToken ||
        data.jwt ||
        (typeof data === "string" ? data : null);

      if (!jwt) {
        throw new Error("No token returned by the server.");
      }

      const profile = {
        name: data.name || data.username || (email ? email.split("@")[0] : "User"),
        email: data.email || email,
      };

      setToken(jwt);
      setUser(profile);
      return { success: true };
    } catch (err) {
      const message =
        err.response?.data?.message ||
        err.response?.data?.error ||
        err.message ||
        "Login failed. Please try again.";
      return { success: false, message };
    } finally {
      setLoading(false);
    }
  };

 const register = async ({
    name,
    email,
    password,
    organizationName
}) => {
    setLoading(true);

    try {
        await apiRegister({
            name,
            email,
            password,
            organizationName
        });

        return { success: true };

    } catch (err) {
        const message =
            err.response?.data?.message ||
            err.response?.data?.error ||
            err.message ||
            "Registration failed.";

        return { success: false, message };

    } finally {
        setLoading(false);
    }
};
  const logout = () => {
    setToken(null);
    setUser(null);
  };

  const value = useMemo(
    () => ({ token, user, loading, login, register, logout }),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [token, user, loading]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return ctx;
}
