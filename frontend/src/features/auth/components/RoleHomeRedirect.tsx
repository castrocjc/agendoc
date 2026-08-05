import {
  Navigate,
} from "react-router-dom";

import {
  getDefaultRouteForRole,
  getSession,
} from "../services/sessionService";

function RoleHomeRedirect() {
  const session = getSession();

  if (!session) {
    return <Navigate to="/login" replace />;
  }

  return (
    <Navigate
      to={getDefaultRouteForRole(session.user.role)}
      replace
    />
  );
}

export default RoleHomeRedirect;
