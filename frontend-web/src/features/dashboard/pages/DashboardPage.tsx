import {
  Calendar,
  CalendarPlus,
  CalendarSearch,
  LogOut,
  Stethoscope,
  UserPlus,
  Users,
} from "lucide-react";
import { useNavigate } from "react-router-dom";

import AppButton from "../../../components/AppButton";
import { clearSession } from "../../auth/services/sessionService";

import "./DashboardPage.css";

function DashboardPage() {
  const navigate = useNavigate();

  function handleLogout(): void {
    const confirmed = window.confirm(
      "¿Deseas cerrar tu sesión en AgenDoc?",
    );

    if (!confirmed) {
      return;
    }

    clearSession();

    navigate("/login", {
      replace: true,
    });
  }

  function handleRegisterDoctor(): void {
    navigate("/doctors/new");
  }

  function handleRegisterPatient(): void {
    navigate("/patients/new");
  }

  function handleSearchPatient(): void {
    navigate("/patients/search");
  }

  function handleMedicalAgenda(): void {
    navigate("/agenda");
  }

  function handleCreateAppointment(): void {
    navigate("/appointments/new");
  }

  function handleAppointmentAgenda(): void {
    navigate("/appointments");
  }

  return (
    <main className="dashboard-page">
      <header className="dashboard-page__header">
        <img
          src="/branding/agendoc-logo.png"
          alt="AgenDoc"
          className="dashboard-page__logo"
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

      <section className="dashboard-page__content">
        <div className="dashboard-page__heading">
          <div>
            <p className="dashboard-page__eyebrow">
              Gestión del consultorio
            </p>

            <h1>Dashboard</h1>

            <p className="dashboard-page__description">
              Accede rápidamente a las principales operaciones de
              AgenDoc.
            </p>
          </div>
        </div>

        <section
          className="dashboard-page__actions"
          aria-label="Operaciones del consultorio"
        >
          <article className="dashboard-page__card">
            <div
              className="dashboard-page__card-icon"
              aria-hidden="true"
            >
              <Stethoscope size={24} />
            </div>

            <div className="dashboard-page__card-content">
              <h2>Registrar médico</h2>

              <p>
                Registra los datos personales, profesionales y de
                contacto de un médico.
              </p>
            </div>

            <AppButton
              type="button"
              fullWidth={false}
              onClick={handleRegisterDoctor}
            >
              Registrar médico
            </AppButton>
          </article>

          <article className="dashboard-page__card">
            <div
              className="dashboard-page__card-icon"
              aria-hidden="true"
            >
              <UserPlus size={24} />
            </div>

            <div className="dashboard-page__card-content">
              <h2>Registrar paciente</h2>

              <p>
                Registra pacientes para gestionar sus próximas citas.
              </p>
            </div>

            <AppButton
              type="button"
              fullWidth={false}
              onClick={handleRegisterPatient}
            >
              Registrar paciente
            </AppButton>
          </article>

          <article className="dashboard-page__card">
            <div
              className="dashboard-page__card-icon"
              aria-hidden="true"
            >
              <Users size={24} />
            </div>

            <div className="dashboard-page__card-content">
              <h2>Buscar paciente</h2>

              <p>
                Consulta pacientes registrados por nombre o documento.
              </p>
            </div>

            <AppButton
              type="button"
              fullWidth={false}
              onClick={handleSearchPatient}
            >
              Buscar paciente
            </AppButton>
          </article>

          <article className="dashboard-page__card">
            <div
              className="dashboard-page__card-icon"
              aria-hidden="true"
            >
              <Calendar size={24} />
            </div>

            <div className="dashboard-page__card-content">
              <h2>Agenda médica</h2>

              <p>
                Configura los bloques de disponibilidad de los médicos.
              </p>
            </div>

            <AppButton
              type="button"
              fullWidth={false}
              onClick={handleMedicalAgenda}
            >
              Gestionar agenda
            </AppButton>
          </article>

          <article className="dashboard-page__card">
            <div
              className="dashboard-page__card-icon"
              aria-hidden="true"
            >
              <CalendarPlus size={24} />
            </div>

            <div className="dashboard-page__card-content">
              <h2>Agendar cita</h2>

              <p>
                Programa una nueva cita seleccionando paciente, médico y horario disponible.
              </p>
            </div>

            <AppButton
              type="button"
              fullWidth={false}
              onClick={handleCreateAppointment}
            >
              Agendar cita
            </AppButton>
          </article>

          <article className="dashboard-page__card">
            <div
              className="dashboard-page__card-icon"
              aria-hidden="true"
            >
              <CalendarSearch size={24} />
            </div>

            <div className="dashboard-page__card-content">
              <h2>Agenda del consultorio</h2>

              <p>
                Consulta las citas registradas por fecha, médico y estado.
              </p>
            </div>

            <AppButton
              type="button"
              fullWidth={false}
              onClick={handleAppointmentAgenda}
            >
              Consultar agenda
            </AppButton>
          </article>

        </section>
      </section>
    </main>
  );
}

export default DashboardPage;