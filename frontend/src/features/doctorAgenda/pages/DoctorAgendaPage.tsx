import {
  useEffect,
  useState,
} from "react";

import {
  CalendarDays,
  Clock3,
  LogOut,
  Stethoscope,
  UserRound,
} from "lucide-react";
import {
  useNavigate,
} from "react-router-dom";

import AppButton from "../../../components/AppButton";
import AppInput from "../../../components/AppInput";
import AppSelect from "../../../components/AppSelect";
import MedicalObservationDialog from "../components/MedicalObservationDialog";
import {
  clearSession,
} from "../../auth/services/sessionService";
import {
  AppointmentServiceError,
  findDoctorAppointments,
} from "../../appointment/services/appointmentService";
import type {
  AppointmentAgendaResponse,
} from "../../appointment/types/appointment.types";

import "./DoctorAgendaPage.css";

const STATUS_OPTIONS = [
  {
    value: "ALL",
    label: "Todos los estados",
  },
  {
    value: "PROGRAMADA",
    label: "Programada",
  },
  {
    value: "CONFIRMADA",
    label: "Confirmada",
  },
  {
    value: "ATENDIDA",
    label: "Atendida",
  },
  {
    value: "CANCELADA",
    label: "Cancelada",
  },
  {
    value: "NO_ASISTIO",
    label: "No asistió",
  },
];

function getToday(): string {
  const today = new Date();

  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, "0");
  const day = String(today.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
}

function formatDate(value: string): string {
  const [year, month, day] = value.split("-");

  return `${day}/${month}/${year}`;
}

function formatTime(value: string): string {
  return value.slice(0, 5);
}

function getStatusClass(statusCode: string): string {
  return `doctor-agenda-page__status doctor-agenda-page__status--${statusCode
    .toLowerCase()
    .replaceAll("_", "-")}`;
}

function canManageMedicalObservation(
  statusCode: string,
): boolean {
  return statusCode === "CONFIRMADA"
    || statusCode === "ATENDIDA";
}

function DoctorAgendaPage() {
  const navigate = useNavigate();

  const [selectedDate, setSelectedDate] = useState(getToday());
  const [selectedStatus, setSelectedStatus] = useState("ALL");

  const [appointments, setAppointments] = useState<
    AppointmentAgendaResponse[]
  >([]);

  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const [
    selectedAppointment,
    setSelectedAppointment,
  ] = useState<AppointmentAgendaResponse | null>(
    null,
  );

  async function loadAppointments(
    date: string,
    status: string,
  ): Promise<void> {
    setIsLoading(true);
    setErrorMessage("");

    try {
      const results = await findDoctorAppointments({
        date,
        status: status === "ALL"
          ? undefined
          : status,
      });

      setAppointments(
        [...results].sort((first, second) =>
          first.startTime.localeCompare(second.startTime),
        ),
      );
    } catch (error) {
      setAppointments([]);

      if (error instanceof AppointmentServiceError) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage(
          "No fue posible consultar tu agenda médica. Inténtalo nuevamente.",
        );
      }
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    let isMounted = true;

    findDoctorAppointments({
      date: getToday(),
    })
      .then((results) => {
        if (!isMounted) {
          return;
        }

        setAppointments(
          [...results].sort((first, second) =>
            first.startTime.localeCompare(second.startTime),
          ),
        );
      })
      .catch((error: unknown) => {
        if (!isMounted) {
          return;
        }

        setAppointments([]);

        if (error instanceof AppointmentServiceError) {
          setErrorMessage(error.message);
        } else {
          setErrorMessage(
            "No fue posible consultar tu agenda médica. Inténtalo nuevamente.",
          );
        }
      })
      .finally(() => {
        if (isMounted) {
          setIsLoading(false);
        }
      });

    return () => {
      isMounted = false;
    };
  }, []);

  function handleLogout(): void {
    clearSession();

    navigate("/login", {
      replace: true,
    });
  }

  function handleDateChange(value: string): void {
    setSelectedDate(value);
  }

  function handleStatusChange(value: string): void {
    setSelectedStatus(value);
  }

  function handleSearch(): void {
    void loadAppointments(
      selectedDate,
      selectedStatus,
    );
  }

  function handleOpenMedicalObservation(
    appointment: AppointmentAgendaResponse,
  ): void {
    setSelectedAppointment(appointment);
  }

  function handleCloseMedicalObservation(): void {
    setSelectedAppointment(null);
  }

  function handleAppointmentAttended(): void {
    setSelectedAppointment(null);

    void loadAppointments(
      selectedDate,
      selectedStatus,
    );
  }

  return (
    <main className="doctor-agenda-page">
      <header className="doctor-agenda-page__header">
        <img
          src="/branding/agendoc-logo.png"
          alt="AgenDoc"
          className="doctor-agenda-page__logo"
        />

        <div className="doctor-agenda-page__header-actions">
          <AppButton
            type="button"
            variant="ghost"
            fullWidth={false}
            onClick={() => {
              navigate("/account");
            }}
          >
            Volver
          </AppButton>

          <AppButton
            type="button"
            variant="ghost"
            fullWidth={false}
            leftIcon={<LogOut size={18} />}
            onClick={handleLogout}
          >
            Cerrar sesión
          </AppButton>
        </div>
      </header>

      <section className="doctor-agenda-page__content">
        <div className="doctor-agenda-page__heading">
          <span
            className="doctor-agenda-page__heading-icon"
            aria-hidden="true"
          >
            <Stethoscope size={28} />
          </span>

          <div>
            <p className="doctor-agenda-page__eyebrow">
              Portal del médico
            </p>

            <h1>Mi agenda médica</h1>

            <p>
              Consulta las citas asignadas a tu agenda para la fecha seleccionada.
            </p>
          </div>
        </div>

        <section className="doctor-agenda-page__filters">
          <AppInput
            id="doctor-agenda-date"
            type="date"
            label="Fecha"
            value={selectedDate}
            onChange={(event) => {
              handleDateChange(event.target.value);
            }}
          />

          <AppSelect
            id="doctor-agenda-status"
            label="Estado"
            value={selectedStatus}
            options={STATUS_OPTIONS}
            onChange={(event) => {
              handleStatusChange(event.target.value);
            }}
          />

          <div className="doctor-agenda-page__filter-action">
            <AppButton
              type="button"
              fullWidth={false}
              leftIcon={<CalendarDays size={18} />}
              onClick={handleSearch}
            >
              Consultar agenda
            </AppButton>
          </div>
        </section>

        <section className="doctor-agenda-page__results">
          <div className="doctor-agenda-page__results-heading">
            <div>
              <h2>
                Agenda del {formatDate(selectedDate)}
              </h2>

              <p>
                Las citas se muestran ordenadas por hora de inicio.
              </p>
            </div>

            <span className="doctor-agenda-page__count">
              {appointments.length}
            </span>
          </div>

          {isLoading && (
            <div className="doctor-agenda-page__state">
              <span className="doctor-agenda-page__spinner" />

              <h3>Consultando agenda</h3>

              <p>
                Estamos cargando las citas asignadas para la fecha seleccionada.
              </p>
            </div>
          )}

          {!isLoading && errorMessage && (
            <div className="doctor-agenda-page__state doctor-agenda-page__state--error">
              <h3>No fue posible consultar la agenda</h3>

              <p>{errorMessage}</p>
            </div>
          )}

          {!isLoading
            && !errorMessage
            && appointments.length === 0 && (
              <div className="doctor-agenda-page__state">
                <CalendarDays size={34} />

                <h3>No tienes citas para esta fecha</h3>

                <p>
                  Selecciona otra fecha o cambia el filtro de estado para revisar tu agenda.
                </p>
              </div>
            )}

          {!isLoading
            && !errorMessage
            && appointments.length > 0 && (
              <>
                <div className="doctor-agenda-page__table-wrapper">
                  <table className="doctor-agenda-page__table">
                    <thead>
                      <tr>
                        <th>Horario</th>
                        <th>Paciente</th>
                        <th>Estado</th>
                        <th>Motivo</th>
                        <th>Acciones</th>
                      </tr>
                    </thead>

                    <tbody>
                      {appointments.map((appointment) => (
                        <tr key={appointment.id}>
                          <td>
                            <span className="doctor-agenda-page__time">
                              {formatTime(appointment.startTime)}
                              {" - "}
                              {formatTime(appointment.endTime)}
                            </span>
                          </td>

                          <td>
                            <span className="doctor-agenda-page__person">
                              {appointment.patientFirstName}
                              {" "}
                              {appointment.patientLastName}
                            </span>
                          </td>

                          <td>
                            <span
                              className={getStatusClass(
                                appointment.statusCode,
                              )}
                            >
                              {appointment.statusName}
                            </span>
                          </td>

                          <td>
                            {appointment.reason || "Sin motivo registrado"}
                          </td>

                          <td>
                            {canManageMedicalObservation(
                              appointment.statusCode,
                            ) ? (
                              <AppButton
                                type="button"
                                variant="secondary"
                                fullWidth={false}
                                onClick={() => {
                                  handleOpenMedicalObservation(
                                    appointment,
                                  );
                                }}
                              >
                                {appointment.statusCode === "ATENDIDA"
                                  ? "Ver observación"
                                  : "Registrar observación"}
                              </AppButton>
                            ) : (
                              <span className="doctor-agenda-page__action-unavailable">
                                No disponible
                              </span>
                            )}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>

                <div className="doctor-agenda-page__cards">
                  {appointments.map((appointment) => (
                    <article
                      key={appointment.id}
                      className="doctor-agenda-page__appointment-card"
                    >
                      <div className="doctor-agenda-page__appointment-card-header">
                        <div>
                          <p className="doctor-agenda-page__appointment-date">
                            {formatDate(
                              appointment.appointmentDate,
                            )}
                          </p>

                          <p className="doctor-agenda-page__appointment-time">
                            {formatTime(appointment.startTime)}
                            {" - "}
                            {formatTime(appointment.endTime)}
                          </p>
                        </div>

                        <span
                          className={getStatusClass(
                            appointment.statusCode,
                          )}
                        >
                          {appointment.statusName}
                        </span>
                      </div>

                      <div className="doctor-agenda-page__appointment-detail">
                        <UserRound size={20} />

                        <div>
                          <span>Paciente</span>

                          <strong>
                            {appointment.patientFirstName}
                            {" "}
                            {appointment.patientLastName}
                          </strong>
                        </div>
                      </div>

                      <div className="doctor-agenda-page__appointment-detail">
                        <Clock3 size={20} />

                        <div>
                          <span>Motivo</span>

                          <strong>
                            {appointment.reason
                              || "Sin motivo registrado"}
                          </strong>
                        </div>
                      </div>

                      {canManageMedicalObservation(
                        appointment.statusCode,
                      ) && (
                        <div className="doctor-agenda-page__appointment-actions">
                          <AppButton
                            type="button"
                            variant="secondary"
                            onClick={() => {
                              handleOpenMedicalObservation(
                                appointment,
                              );
                            }}
                          >
                            {appointment.statusCode === "ATENDIDA"
                              ? "Ver o editar observación"
                              : "Registrar observación"}
                          </AppButton>
                        </div>
                      )}
                    </article>
                  ))}
                </div>
              </>
            )}
        </section>
      </section>

      <MedicalObservationDialog
        appointment={selectedAppointment}
        open={selectedAppointment !== null}
        onClose={handleCloseMedicalObservation}
        onAttended={handleAppointmentAttended}
      />
    </main>
  );
}

export default DoctorAgendaPage;
