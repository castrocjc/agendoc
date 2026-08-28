import {
  Building2,
  LogOut,
  Stethoscope,
  UserRound,
  Users,
} from "lucide-react";
import {
  useNavigate,
} from "react-router-dom";

import AppButton from "../../../components/AppButton";
import {
  clearSession,
  getAuthenticatedUser,
} from "../../auth/services/sessionService";
import AdministrativeModuleCard from "../components/AdministrativeModuleCard";
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
              Gestiona las funciones administrativas habilitadas
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

        <section
          className="admin-workspace-page__context"
          aria-label="Contexto administrativo"
        >
          <div className="admin-workspace-page__context-icon">
            <Building2 size={22} />
          </div>

          <div>
            <span>Contexto administrativo</span>

            <strong>
              {user
                ? `Consultorio #${user.clinicId}`
                : "Consultorio autenticado"}
            </strong>
          </div>
        </section>

        <section className="admin-workspace-page__modules">
          <div className="admin-workspace-page__section-heading">
            <h2>Módulos administrativos</h2>

            <p>
              Las funciones disponibles evolucionarán
              progresivamente durante Release 1.1.
            </p>
          </div>

          <div className="admin-workspace-page__grid">
            <AdministrativeModuleCard
              title="Especialidades médicas"
              description="Gestiona las especialidades disponibles en el consultorio."
              icon={Stethoscope}
              status="coming-soon"
            />

            <AdministrativeModuleCard
              title="Usuarios"
              description="Gestiona identidades, roles y acceso al consultorio."
              icon={Users}
              status="coming-soon"
            />

            <AdministrativeModuleCard
              title="Médicos"
              description="Gestiona los médicos registrados en el consultorio."
              icon={UserRound}
              status="coming-soon"
            />

            <AdministrativeModuleCard
              title="Pacientes"
              description="Gestiona los pacientes registrados en el consultorio."
              icon={Users}
              status="coming-soon"
            />
          </div>
        </section>

        <section className="admin-workspace-page__status">
          <span>Sesión administrativa</span>

          <strong>
            {user?.email ?? "Usuario autenticado"}
          </strong>
        </section>
      </section>
    </main>
  );
}

export default AdminWorkspacePage;
