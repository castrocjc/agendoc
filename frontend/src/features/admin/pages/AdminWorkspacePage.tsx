import {
  Building2,
  LogOut,
  ShieldCheck,
} from "lucide-react";
import {
  useNavigate,
} from "react-router-dom";
import AppButton from "../../../components/AppButton";
import AppCard from "../../../components/AppCard";
import {
  clearSession,
  getAuthenticatedUser,
} from "../../auth/services/sessionService";
import "./AdminWorkspacePage.css";

function AdminWorkspacePage() {
  const navigate = useNavigate();
  const user = getAuthenticatedUser();

  function handleLogout(): void {
    clearSession();

    navigate("/login", {
      replace: true,
    });
  }

  return (
    <main className="admin-workspace-page">
      <section className="admin-workspace-page__shell">
        <header className="admin-workspace-page__header">
          <div>
            <span className="admin-workspace-page__eyebrow">
              Administración
            </span>

            <h1>Workspace administrativo</h1>

            <p>
              Accede a las funciones administrativas habilitadas
              para tu consultorio.
            </p>
          </div>

          <AppButton
            type="button"
            variant="ghost"
            fullWidth={false}
            leftIcon={<LogOut size={18} />}
            onClick={handleLogout}
          >
            Cerrar sesión
          </AppButton>
        </header>

        <section className="admin-workspace-page__grid">
          <AppCard
            className="admin-workspace-page__card"
            elevation="low"
          >
            <div className="admin-workspace-page__card-icon">
              <ShieldCheck size={28} />
            </div>

            <div>
              <h2>Acceso administrativo habilitado</h2>

              <p>
                Tu identidad tiene acceso al Workspace de
                Administración del consultorio autenticado.
              </p>
            </div>
          </AppCard>

          <AppCard
            className="admin-workspace-page__card"
            elevation="low"
          >
            <div className="admin-workspace-page__card-icon">
              <Building2 size={28} />
            </div>

            <div>
              <h2>Consultorio actual</h2>

              <p>
                Las operaciones administrativas se ejecutarán
                dentro del contexto del consultorio autenticado.
              </p>

              {user && (
                <span className="admin-workspace-page__clinic">
                  Consultorio #{user.clinicId}
                </span>
              )}
            </div>
          </AppCard>
        </section>

        <section className="admin-workspace-page__status">
          <span>
            Sesión administrativa
          </span>

          <strong>
            {user?.email ?? "Usuario autenticado"}
          </strong>

          <p>
            Las funciones administrativas se incorporarán
            progresivamente durante Release 1.1.
          </p>
        </section>
      </section>
    </main>
  );
}

export default AdminWorkspacePage;
