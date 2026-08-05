import {
  CalendarCheck2,
  LogOut,
  ShieldCheck,
  UserRound,
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

import "./AccountHomePage.css";

function AccountHomePage() {
  const navigate = useNavigate();
  const user = getAuthenticatedUser();

  function handleLogout(): void {
    clearSession();

    navigate("/login", {
      replace: true,
    });
  }

  const isPatient = user?.role === "PATIENT";

  return (
    <main className="account-home-page">
      <header className="account-home-page__header">
        <img
          src="/branding/agendoc-logo.png"
          alt="AgenDoc"
          className="account-home-page__logo"
        />

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

      <section className="account-home-page__content">
        <AppCard
          className="account-home-page__welcome"
          elevation="medium"
        >
          <span
            className="account-home-page__welcome-icon"
            aria-hidden="true"
          >
            <UserRound size={30} />
          </span>

          <div>
            <p className="account-home-page__eyebrow">
              {isPatient
                ? "Portal del paciente"
                : "Cuenta de AgenDoc"}
            </p>

            <h1>
              Bienvenido a AgenDoc
            </h1>

            <p>
              Has iniciado sesión como{" "}
              <strong>{user?.email}</strong>.
            </p>
          </div>
        </AppCard>

        <section className="account-home-page__grid">
          <AppCard
            className="account-home-page__card"
            elevation="low"
          >
            <span
              className="account-home-page__card-icon"
              aria-hidden="true"
            >
              <ShieldCheck size={25} />
            </span>

            <div>
              <h2>Acceso protegido</h2>

              <p>
                Tu cuenta solo puede utilizar las funciones habilitadas para su rol.
              </p>
            </div>
          </AppCard>

          <AppCard
            className="account-home-page__card"
            elevation="low"
          >
            <span
              className="account-home-page__card-icon"
              aria-hidden="true"
            >
              <CalendarCheck2 size={25} />
            </span>

            <div>
              <h2>
                {isPatient
                  ? "Tus citas"
                  : "Tu espacio de trabajo"}
              </h2>

              <p>
                {isPatient
                  ? "La consulta y administración de tus citas estará disponible en una próxima historia del portal del paciente."
                  : "El portal específico para tu rol estará disponible en una próxima historia."}
              </p>
            </div>
          </AppCard>
        </section>

        <p className="account-home-page__notice">
          Las operaciones administrativas del consultorio están reservadas para usuarios autorizados.
        </p>
      </section>
    </main>
  );
}

export default AccountHomePage;
