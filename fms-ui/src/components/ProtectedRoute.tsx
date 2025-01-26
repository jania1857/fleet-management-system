import React from "react";
import { Navigate } from "react-router-dom";
import { UserRole } from "../types/User";

interface ProtectedRouteProps {
    role: UserRole;
    component: React.ComponentType;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ role, component: Component }) => {
    const user = JSON.parse(localStorage.getItem("user") || "{}");

    if (!user || user.role !== role) {
        return <Navigate to="/" />;
    }

    return <Component />;
};

export default ProtectedRoute;