import {
  useEffect,
  useState,
} from "react";
import type { ReactNode } from "react";
import { Navigate } from "react-router-dom";

import {
  isAuthenticated,
  subscribeToSessionCleared,
} from "../services/sessionService";

interface ProtectedRouteProps {
  children: ReactNode;
}

function ProtectedRoute({ children }: ProtectedRouteProps) {
  const [authenticated, setAuthenticated] =
    useState(isAuthenticated);

  useEffect(() => {
    return subscribeToSessionCleared(() => {
      setAuthenticated(false);
    });
  }, []);

  if (!authenticated) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
}

export default ProtectedRoute;
