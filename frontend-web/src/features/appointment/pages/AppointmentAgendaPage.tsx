import {
  useEffect,
  useMemo,
  useState,
} from "react";
import type {
  FormEvent,
} from "react";

import {
  ArrowLeft,
  CalendarDays,
  Clock3,
  Search,
  Stethoscope,
  UserRound,
} from "lucide-react";
import { useNavigate } from "react-router-dom";

import AppButton from "../../../components/AppButton";
import AppInput from "../../../components/AppInput";
import AppSelect from "../../../components/AppSelect";
import {
  DoctorServiceError,
  getDoctors,
} from "../../doctor/services/doctorService";
import type {
  DoctorResponse,
} from "../../doctor/types/doctor.types";
import {
  AppointmentServiceError,
  findAppointments,
} from "../services/appointmentService";
import type {
  AppointmentAgendaResponse,
} from "../types/appointment.types";

import "./AppointmentAgendaPage.css";

const STATUS_OPTIONS = [
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

function getAppointmentStatusClass(
  statusCode: string,
): string {
  return `appointment-agenda-page__status appointment-agenda-page__status--${statusCode
    .toLowerCase()
    .replaceAll("_", "-")}`;
}

function AppointmentAgendaPage() {
  const navigate = useNavigate();

  const [doctors, setDoctors] = useState<DoctorResponse[]>([]);
  const [isLoadingDoctors, setIsLoadingDoctors] = useState(true);
  const [doctorError, setDoctorError] = useState("");

  const [selectedDate, setSelectedDate] = useState(getToday());
  const [selectedDoctorId, setSelectedDoctorId] = useState("");
  const [selectedStatus, setSelectedStatus] = useState("");

  const [appointments, setAppointments] =
    useState<AppointmentAgendaResponse[]>([]);

  const [isLoadingAppointments, setIsLoadingAppointments] =
    useState(false);

  const [appointmentError, setAppointmentError] = useState("");
  const [searchPerformed, setSearchPerformed] = useState(false);
  const [dateError, setDateError] = useState("");

  const doctorOptions = useMemo(
    () =>
      doctors.map((doctor) => ({
        value: doctor.id.toString(),
        label: `${doctor.firstName} ${doctor.lastName} · ${doctor.specialtyName}`,
      })),
    [doctors],
  );

  useEffect(() => {
    let isMounted = true;

    async function loadDoctors(): Promise<void> {
      setIsLoadingDoctors(true);
      setDoctorError("");

      try {
        const results = await getDoctors();

        if (isMounted) {
          setDoctors(results);
        }
      } catch (error) {
        if (!isMounted) {
          return;
        }

        setDoctors([]);

        if (error instanceof DoctorServiceError) {
          setDoctorError(error.message);
        } else {
          setDoctorError(
            "No fue posible cargar los médicos. Inténtalo nuevamente.",
          );
        }
      } finally {
        if (isMounted) {
          setIsLoadingDoctors(false);
        }
      }
    }

    void loadDoctors();

    return () => {
      isMounted = false;
    };
  }, []);

  function handleBack(): void {
    navigate("/dashboard");
  }

  function clearSearchResult(): void {
    setAppointments([]);
    setAppointmentError("");
    setSearchPerformed(false);
  }

  function handleDateChange(value: string): void {
    setSelectedDate(value);
    setDateError("");
    clearSearchResult();
  }

  function handleDoctorChange(value: string): void {
    setSelectedDoctorId(value);
    clearSearchResult();
  }

  function handleStatusChange(value: string): void {
    setSelectedStatus(value);
    clearSearchResult();
  }

  async function handleSearch(
    event: FormEvent<HTMLFormElement>,
  ): Promise<void> {
    event.preventDefault();

    setDateError("");
    setAppointmentError("");
    setAppointments([]);
    setSearchPerformed(true);

    if (!selectedDate) {
      setDateError(
        "Selecciona la fecha que deseas consultar.",
      );
      return;
    }

    setIsLoadingAppointments(true);

    try {
      const results = await findAppointments({
        date: selectedDate,
        doctorId: selectedDoctorId
          ? Number(selectedDoctorId)
          : undefined,
        status: selectedStatus || undefined,
      });

      setAppointments(
        [...results].sort((firstAppointment, secondAppointment) =>
          firstAppointment.startTime.localeCompare(
            secondAppointment.startTime,
          ),
        ),
      );
    } catch (error) {
      if (error instanceof AppointmentServiceError) {
        setAppointmentError(error.message);
      } else {
        setAppointmentError(
          "No fue posible consultar la agenda del consultorio.",
        );
      }
    } finally {
      setIsLoadingAppointments(false);
    }
  }

  return (
    <main className="appointment-agenda-page">
      <header className="appointment-agenda-page__header">
        <img
          src="/branding/agendoc-logo.png"
          alt="AgenDoc"
          className="appointment-agenda-page__logo"
        />

        <AppButton
          type="button"
          variant="ghost"
          fullWidth={false}
          leftIcon={<ArrowLeft size={18} />}
          onClick={handleBack}
        >
          Volver
        </AppButton>
      </header>

      <section className="appointment-agenda-page__content">
        <div className="appointment-agenda-page__heading">
          <div
            className="appointment-agenda-page__heading-icon"
            aria-hidden="true"
          >
            <CalendarDays size={24} />
          </div>

          <div>
            <h1>Agenda del consultorio</h1>

            <p>
              Consulta las citas programadas y filtra la información
              por fecha, médico o estado.
            </p>
          </div>
        </div>

        <section
          className="appointment-agenda-page__card"
          aria-labelledby="appointment-filters-title"
        >
          <div className="appointment-agenda-page__section-heading">
            <div>
              <h2 id="appointment-filters-title">
                Filtros de consulta
              </h2>

              <p>
                La fecha es obligatoria. Los demás filtros son
                opcionales.
              </p>
            </div>
          </div>

          <form
            className="appointment-agenda-page__filters"
            onSubmit={(event) => void handleSearch(event)}
            noValidate
          >
            <AppInput
              id="appointmentAgendaDate"
              name="appointmentAgendaDate"
              label="Fecha"
              type="date"
              value={selectedDate}
              error={dateError}
              disabled={isLoadingAppointments}
              onChange={(event) =>
                handleDateChange(event.target.value)
              }
            />

            <AppSelect
              id="appointmentAgendaDoctor"
              name="appointmentAgendaDoctor"
              label="Médico"
              value={selectedDoctorId}
              options={doctorOptions}
              placeholder="Todos los médicos"
              helperText={
                doctorError
                  ? doctorError
                  : "Opcional"
              }
              disabled={
                isLoadingDoctors
                || isLoadingAppointments
                || doctors.length === 0
              }
              onChange={(event) =>
                handleDoctorChange(event.target.value)
              }
            />

            <AppSelect
              id="appointmentAgendaStatus"
              name="appointmentAgendaStatus"
              label="Estado"
              value={selectedStatus}
              options={STATUS_OPTIONS}
              placeholder="Todos los estados"
              helperText="Opcional"
              disabled={isLoadingAppointments}
              onChange={(event) =>
                handleStatusChange(event.target.value)
              }
            />

            <div className="appointment-agenda-page__filter-action">
              <AppButton
                type="submit"
                fullWidth={false}
                leftIcon={<Search size={18} />}
                isLoading={isLoadingAppointments}
                disabled={isLoadingAppointments}
              >
                Consultar agenda
              </AppButton>
            </div>
          </form>
        </section>

        <section
          className="appointment-agenda-page__card"
          aria-labelledby="appointment-results-title"
          aria-live="polite"
          aria-busy={isLoadingAppointments}
        >
          <div className="appointment-agenda-page__section-heading">
            <div>
              <h2 id="appointment-results-title">
                Citas registradas
              </h2>

              <p>
                Las citas se muestran ordenadas por hora de inicio.
              </p>
            </div>

            {!isLoadingAppointments
              && searchPerformed
              && !appointmentError
              && appointments.length > 0 && (
                <span className="appointment-agenda-page__count">
                  {appointments.length}
                </span>
              )}
          </div>

          {!searchPerformed && !isLoadingAppointments && (
            <div className="appointment-agenda-page__state">
              <CalendarDays size={32} aria-hidden="true" />

              <h3>Consulta la agenda</h3>

              <p>
                Selecciona los filtros y presiona Consultar agenda
                para visualizar las citas.
              </p>
            </div>
          )}

          {isLoadingAppointments && (
            <div
              className="appointment-agenda-page__state"
              role="status"
            >
              <div
                className="appointment-agenda-page__spinner"
                aria-hidden="true"
              />

              <h3>Consultando agenda</h3>

              <p>
                Espera un momento mientras consultamos las citas.
              </p>
            </div>
          )}

          {!isLoadingAppointments && appointmentError && (
            <div
              className="appointment-agenda-page__state appointment-agenda-page__state--error"
              role="alert"
            >
              <CalendarDays size={32} aria-hidden="true" />

              <h3>No pudimos consultar la agenda</h3>

              <p>{appointmentError}</p>
            </div>
          )}

          {!isLoadingAppointments
            && searchPerformed
            && !appointmentError
            && appointments.length === 0 && (
              <div className="appointment-agenda-page__state">
                <Clock3 size={32} aria-hidden="true" />

                <h3>No hay citas registradas</h3>

                <p>
                  No encontramos citas que coincidan con los filtros
                  seleccionados.
                </p>
              </div>
            )}

          {!isLoadingAppointments
            && !appointmentError
            && appointments.length > 0 && (
              <>
                <div className="appointment-agenda-page__table-wrapper">
                  <table className="appointment-agenda-page__table">
                    <thead>
                      <tr>
                        <th>Hora</th>
                        <th>Paciente</th>
                        <th>Médico</th>
                        <th>Especialidad</th>
                        <th>Estado</th>
                        <th>Motivo</th>
                      </tr>
                    </thead>

                    <tbody>
                      {appointments.map((appointment) => (
                        <tr key={appointment.id}>
                          <td>
                            <span className="appointment-agenda-page__time">
                              {formatTime(appointment.startTime)}
                              {" - "}
                              {formatTime(appointment.endTime)}
                            </span>
                          </td>

                          <td>
                            <span className="appointment-agenda-page__person">
                              {appointment.patientFirstName}
                              {" "}
                              {appointment.patientLastName}
                            </span>
                          </td>

                          <td>
                            <span className="appointment-agenda-page__person">
                              {appointment.doctorFirstName}
                              {" "}
                              {appointment.doctorLastName}
                            </span>
                          </td>

                          <td>{appointment.specialtyName}</td>

                          <td>
                            <span
                              className={getAppointmentStatusClass(
                                appointment.statusCode,
                              )}
                            >
                              {appointment.statusName}
                            </span>
                          </td>

                          <td>
                            {appointment.reason?.trim()
                              || "Sin motivo registrado"}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>

                <div className="appointment-agenda-page__cards">
                  {appointments.map((appointment) => (
                    <article
                      key={appointment.id}
                      className="appointment-agenda-page__appointment-card"
                    >
                      <div className="appointment-agenda-page__appointment-card-header">
                        <div>
                          <p className="appointment-agenda-page__appointment-date">
                            {formatDate(
                              appointment.appointmentDate,
                            )}
                          </p>

                          <p className="appointment-agenda-page__appointment-time">
                            {formatTime(appointment.startTime)}
                            {" - "}
                            {formatTime(appointment.endTime)}
                          </p>
                        </div>

                        <span
                          className={getAppointmentStatusClass(
                            appointment.statusCode,
                          )}
                        >
                          {appointment.statusName}
                        </span>
                      </div>

                      <div className="appointment-agenda-page__appointment-detail">
                        <UserRound
                          size={18}
                          aria-hidden="true"
                        />

                        <div>
                          <span>Paciente</span>

                          <strong>
                            {appointment.patientFirstName}
                            {" "}
                            {appointment.patientLastName}
                          </strong>
                        </div>
                      </div>

                      <div className="appointment-agenda-page__appointment-detail">
                        <Stethoscope
                          size={18}
                          aria-hidden="true"
                        />

                        <div>
                          <span>Médico</span>

                          <strong>
                            {appointment.doctorFirstName}
                            {" "}
                            {appointment.doctorLastName}
                          </strong>

                          <small>
                            {appointment.specialtyName}
                          </small>
                        </div>
                      </div>

                      <div className="appointment-agenda-page__appointment-reason">
                        <span>Motivo</span>

                        <p>
                          {appointment.reason?.trim()
                            || "Sin motivo registrado"}
                        </p>
                      </div>
                    </article>
                  ))}
                </div>
              </>
            )}
        </section>
      </section>
    </main>
  );
}

export default AppointmentAgendaPage;