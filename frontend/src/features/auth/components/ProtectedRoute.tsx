import {
  useEffect,
  useState,
} from "react";
import type {
  ReactNode,
} from "react";
import {
  Navigate,
} from "react-router-dom";

import {
  getDefaultRouteForRole,
  getSession,
  subscribeToSessionCleared,
} from "../services/sessionService";
import type {
  UserRole,
} from "../types/auth.types";

interface ProtectedRouteProps {
  children: ReactNode;
  allowedRoles?: UserRole[];
}

function ProtectedRoute({
  children,
  allowedRoles,
}: ProtectedRouteProps) {
  const [session, setSession] = useState(getSession);

  useEffect(() => {
    return subscribeToSessionCleared(() => {
      setSession(null);
    });
  }, []);

  if (!session) {
    return <Navigate to="/login" replace />;
  }

  if (
    allowedRoles &&
    !allowedRoles.includes(session.user.role)
  ) {
    return (
      <Navigate
        to={getDefaultRouteForRole(session.user.role)}
        replace
      />
    );
  }

  return <>{children}</>;
}

export default ProtectedRoute;
