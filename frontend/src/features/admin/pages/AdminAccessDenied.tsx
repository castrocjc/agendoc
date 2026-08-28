import {
  ShieldX,
} from "lucide-react";
import {
  useNavigate,
} from "react-router-dom";

import AppButton from "../../../components/AppButton";
import AppCard from "../../../components/AppCard";
import {
  getAuthenticatedUser,
  getDefaultRouteForRole,
} from "../../auth/services/sessionService";
import "./AdminAccessDenied.css";

function AdminAccessDenied() {
  const navigate = useNavigate();
  const user = getAuthenticatedUser();

  function handleReturn(): void {
    if (!user) {
      navigate("/login", {
        replace: true,
      });

      return;
    }

    navigate(
      getDefaultRouteForRole(user.role),
      {
        replace: true,
      },
    );
  }

  return (
    <main className="admin-access-denied">
      <AppCard
        className="admin-access-denied__card"
        elevation="low"
      >
        <div className="admin-access-denied__icon">
          <ShieldX size={32} />
        </div>

        <span className="admin-access-denied__eyebrow">
          Administración
        </span>

        <h1>Acceso restringido</h1>

        <p>
          Tu cuenta no tiene autorización para acceder al
          Workspace de Administración.
        </p>

        <div className="admin-access-denied__action">
          <AppButton
            type="button"
            fullWidth={false}
            onClick={handleReturn}
          >
            Volver a mi espacio
          </AppButton>
        </div>
      </AppCard>
    </main>
  );
}

export default AdminAccessDenied;
