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
  unauthorizedElement?: ReactNode;
}

function ProtectedRoute({
  children,
  allowedRoles,
  unauthorizedElement,
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
    if (unauthorizedElement) {
      return <>{unauthorizedElement}</>;
    }

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
