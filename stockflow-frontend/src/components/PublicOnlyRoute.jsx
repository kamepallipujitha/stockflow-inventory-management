import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function PublicOnlyRoute({ children }) {
  const { token } = useAuth();
  const location = useLocation();
  const from = location.state?.from || "/dashboard";

  if (token) {
    return <Navigate to={from} replace />;
  }

  return children;
}
