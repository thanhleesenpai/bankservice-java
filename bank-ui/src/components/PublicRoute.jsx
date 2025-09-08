import { Navigate, Outlet } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

const PublicRoute = ({ children }) => {
  const token = localStorage.getItem("token");

  if (!token) {
    // If no token, allow access to public routes (Login, Register)
    return children;
  }

  try {
    const decoded = jwtDecode(token);
    const userRole = decoded.role;

    // Redirect based on the user's role
    if (userRole === "ADMIN") {
      return <Navigate to="/admin-home" replace />;
    } else if (userRole === "USER") {
      return <Navigate to="/user-home" replace />;
    } else {
      // For any other roles or if role is missing, redirect to login
      return <Navigate to="/login" replace />;
    }
  } catch (error) {
    // If token is invalid or expired, redirect to login
    console.error("Invalid token:", error);
    return <Navigate to="/login" replace />;
  }
};

export default PublicRoute;