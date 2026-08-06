import {
  useCallback,
  useEffect,
  useMemo,
  useState,
} from "react";

import {
  ArrowLeft,
  CalendarDays,
  CalendarPlus,
  CircleAlert,
  Clock3,
  RefreshCw,
  Stethoscope,
} from "lucide-react";
import {
  useNavigate,
} from "react-router-dom";

import AppButton from "../../../components/AppButton";
import AppCard from "../../../components/AppCard";
import {
  AppointmentServiceError,
  findPatientAppointments,
} from "../../appointment/services/appointmentService";
import type {
  AppointmentStatusCode,
  PatientAppointmentResponse,
} from "../../appointment/types/appointment.types";
import type {
  PatientAppointmentsFilter,
  PatientAppointmentsStatus,
} from "../types/patientAppointments.types";

import "./PatientAppointmentsPage.css";

interface FilterOption {
  key: PatientAppointmentsFilter;
  label: string;
}

const FILTER_OPTIONS: FilterOption[] = [
  {
    key: "upcoming",
    label: "Próximas",
  },
  {
    key: "previous",
    label: "Anteriores",
  },
  {
    key: "cancelled",
    label: "Canceladas",
  },
  {
    key: "all",
    label: "Todas",
  },
];

const ACTIVE_APPOINTMENT_STATUSES:
  AppointmentStatusCode[] = [
    "PROGRAMADA",
    "CONFIRMADA",
  ];

function buildAppointmentDateTime(
  appointment: PatientAppointmentResponse,
): Date {
  return new Date(
    `${appointment.appointmentDate}T${appointment.startTime}`,
  );
}

function isUpcomingAppointment(
  appointment: PatientAppointmentResponse,
  now: Date,
): boolean {
  return (
    ACTIVE_APPOINTMENT_STATUSES.includes(
      appointment.statusCode,
    )
    && buildAppointmentDateTime(appointment) >= now
  );
}

function matchesFilter(
  appointment: PatientAppointmentResponse,
  filter: PatientAppointmentsFilter,
  now: Date,
): boolean {
  if (filter === "all") {
    return true;
  }

  if (filter === "cancelled") {
    return appointment.statusCode === "CANCELADA";
  }

  if (filter === "upcoming") {
    return isUpcomingAppointment(
      appointment,
      now,
    );
  }

  return (
    appointment.statusCode !== "CANCELADA"
    && !isUpcomingAppointment(
      appointment,
      now,
    )
  );
}

function sortAppointments(
  appointments: PatientAppointmentResponse[],
  filter: PatientAppointmentsFilter,
): PatientAppointmentResponse[] {
  return [...appointments].sort(
    (first, second) => {
      const firstTime =
        buildAppointmentDateTime(first).getTime();

      const secondTime =
        buildAppointmentDateTime(second).getTime();

      if (filter === "upcoming") {
        return firstTime - secondTime;
      }

      return secondTime - firstTime;
    },
  );
}

function formatLongDate(value: string): string {
  const date = new Date(`${value}T00:00:00`);

  const formattedDate = new Intl.DateTimeFormat(
    "es-MX",
    {
      weekday: "long",
      day: "numeric",
      month: "long",
      year: "numeric",
    },
  ).format(date);

  return (
    formattedDate.charAt(0).toUpperCase()
    + formattedDate.slice(1)
  );
}

function formatTime(value: string): string {
  return value.slice(0, 5);
}

function getDoctorName(
  appointment: PatientAppointmentResponse,
): string {
  return `Dr. ${appointment.doctorFirstName} ${appointment.doctorLastName}`;
}

function getStatusClassName(
  statusCode: AppointmentStatusCode,
): string {
  return statusCode
    .toLowerCase()
    .replace("_", "-");
}

function getEmptyStateCopy(
  filter: PatientAppointmentsFilter,
): {
  title: string;
  description: string;
} {
  switch (filter) {
    case "upcoming":
      return {
        title: "No tienes próximas citas",
        description:
          "Cuando reserves una nueva cita aparecerá en esta sección.",
      };

    case "previous":
      return {
        title: "Aún no tienes citas anteriores",
        description:
          "Aquí podrás revisar las citas que ya finalizaron.",
      };

    case "cancelled":
      return {
        title: "No tienes citas canceladas",
        description:
          "Las citas canceladas permanecerán disponibles para tu consulta.",
      };

    default:
      return {
        title: "Todavía no tienes citas",
        description:
          "Reserva tu primera cita para comenzar a utilizar tu portal.",
      };
  }
}

function PatientAppointmentsPage() {
  const navigate = useNavigate();

  const [appointments, setAppointments] =
    useState<PatientAppointmentResponse[]>([]);

  const [status, setStatus] =
    useState<PatientAppointmentsStatus>("loading");

  const [errorMessage, setErrorMessage] =
    useState("");

  const [selectedFilter, setSelectedFilter] =
    useState<PatientAppointmentsFilter>("upcoming");

  const handleAppointmentsSuccess = useCallback(
    (response: PatientAppointmentResponse[]): void => {
      setAppointments(response);
      setStatus("success");
    },
    [],
  );

  const handleAppointmentsError = useCallback(
    (error: unknown): void => {
      const message =
        error instanceof AppointmentServiceError
          ? error.message
          : "No fue posible consultar tus citas. Inténtalo nuevamente.";

      setAppointments([]);
      setErrorMessage(message);
      setStatus("error");
    },
    [],
  );

  const loadAppointments = useCallback(() => {
    setStatus("loading");
    setErrorMessage("");

    findPatientAppointments()
      .then(handleAppointmentsSuccess)
      .catch(handleAppointmentsError);
  }, [
    handleAppointmentsError,
    handleAppointmentsSuccess,
  ]);

  useEffect(() => {
    let active = true;

    findPatientAppointments()
      .then((response) => {
        if (active) {
          handleAppointmentsSuccess(response);
        }
      })
      .catch((error: unknown) => {
        if (active) {
          handleAppointmentsError(error);
        }
      });

    return () => {
      active = false;
    };
  }, [
    handleAppointmentsError,
    handleAppointmentsSuccess,
  ]);

  const visibleAppointments = useMemo(() => {
    const now = new Date();

    return sortAppointments(
      appointments.filter((appointment) =>
        matchesFilter(
          appointment,
          selectedFilter,
          now,
        ),
      ),
      selectedFilter,
    );
  }, [
    appointments,
    selectedFilter,
  ]);

  const filterCounts = useMemo(() => {
    const now = new Date();

    return FILTER_OPTIONS.reduce<
      Record<PatientAppointmentsFilter, number>
    >(
      (counts, filter) => ({
        ...counts,
        [filter.key]: appointments.filter(
          (appointment) =>
            matchesFilter(
              appointment,
              filter.key,
              now,
            ),
        ).length,
      }),
      {
        upcoming: 0,
        previous: 0,
        cancelled: 0,
        all: 0,
      },
    );
  }, [appointments]);

  const emptyState =
    getEmptyStateCopy(selectedFilter);

  return (
    <main className="patient-appointments-page">
      <header className="patient-appointments-page__header">
        <img
          src="/branding/agendoc-logo.png"
          alt="AgenDoc"
          className="patient-appointments-page__logo"
        />

        <AppButton
          type="button"
          variant="ghost"
          fullWidth={false}
          leftIcon={<ArrowLeft size={18} />}
          onClick={() => {
            navigate("/account");
          }}
        >
          Volver al portal
        </AppButton>
      </header>

      <section className="patient-appointments-page__content">
        <section className="patient-appointments-page__intro">
          <div>
            <p className="patient-appointments-page__eyebrow">
              Portal del paciente
            </p>

            <h1>Mis citas</h1>

            <p>
              Consulta tus próximas citas y revisa tu historial de reservas.
            </p>
          </div>

          <AppButton
            type="button"
            fullWidth={false}
            leftIcon={<CalendarPlus size={18} />}
            onClick={() => {
              navigate("/account/appointments/new");
            }}
          >
            Reservar nueva cita
          </AppButton>
        </section>

        {status === "loading" && (
          <AppCard
            className="patient-appointments-page__feedback"
            elevation="low"
          >
            <span
              className="patient-appointments-page__feedback-icon patient-appointments-page__feedback-icon--loading"
              aria-hidden="true"
            >
              <RefreshCw size={25} />
            </span>

            <div>
              <h2>Consultando tus citas</h2>

              <p>
                Estamos recuperando la información de tu cuenta.
              </p>
            </div>
          </AppCard>
        )}

        {status === "error" && (
          <AppCard
            className="patient-appointments-page__feedback patient-appointments-page__feedback--error"
            elevation="low"
          >
            <span
              className="patient-appointments-page__feedback-icon patient-appointments-page__feedback-icon--error"
              aria-hidden="true"
            >
              <CircleAlert size={25} />
            </span>

            <div className="patient-appointments-page__feedback-content">
              <h2>No pudimos consultar tus citas</h2>

              <p>{errorMessage}</p>

              <AppButton
                type="button"
                fullWidth={false}
                leftIcon={<RefreshCw size={17} />}
                onClick={loadAppointments}
              >
                Intentar nuevamente
              </AppButton>
            </div>
          </AppCard>
        )}

        {status === "success" && (
          <>
            <div
              className="patient-appointments-page__filters"
              role="tablist"
              aria-label="Filtrar citas"
            >
              {FILTER_OPTIONS.map((filter) => {
                const selected =
                  selectedFilter === filter.key;

                return (
                  <button
                    key={filter.key}
                    type="button"
                    role="tab"
                    aria-selected={selected}
                    className={[
                      "patient-appointments-page__filter",
                      selected
                        ? "patient-appointments-page__filter--active"
                        : "",
                    ]
                      .filter(Boolean)
                      .join(" ")}
                    onClick={() => {
                      setSelectedFilter(filter.key);
                    }}
                  >
                    <span>{filter.label}</span>

                    <span className="patient-appointments-page__filter-count">
                      {filterCounts[filter.key]}
                    </span>
                  </button>
                );
              })}
            </div>

            {visibleAppointments.length === 0 ? (
              <AppCard
                className="patient-appointments-page__empty"
                elevation="low"
              >
                <span
                  className="patient-appointments-page__empty-icon"
                  aria-hidden="true"
                >
                  <CalendarDays size={30} />
                </span>

                <h2>{emptyState.title}</h2>

                <p>{emptyState.description}</p>

                <AppButton
                  type="button"
                  fullWidth={false}
                  leftIcon={<CalendarPlus size={18} />}
                  onClick={() => {
                    navigate("/account/appointments/new");
                  }}
                >
                  Reservar una cita
                </AppButton>
              </AppCard>
            ) : (
              <section
                className="patient-appointments-page__list"
                aria-label="Listado de citas"
              >
                {visibleAppointments.map(
                  (appointment) => (
                    <AppCard
                      key={appointment.id}
                      className="patient-appointments-page__appointment"
                      elevation="low"
                    >
                      <div className="patient-appointments-page__appointment-header">
                        <div>
                          <p className="patient-appointments-page__date">
                            {formatLongDate(
                              appointment.appointmentDate,
                            )}
                          </p>

                          <p className="patient-appointments-page__time">
                            <Clock3 size={17} />

                            <span>
                              {formatTime(
                                appointment.startTime,
                              )}
                              {" - "}
                              {formatTime(
                                appointment.endTime,
                              )}
                            </span>
                          </p>
                        </div>

                        <span
                          className={`patient-appointments-page__status patient-appointments-page__status--${getStatusClassName(
                            appointment.statusCode,
                          )}`}
                        >
                          {appointment.statusName}
                        </span>
                      </div>

                      <div className="patient-appointments-page__doctor">
                        <span
                          className="patient-appointments-page__doctor-icon"
                          aria-hidden="true"
                        >
                          <Stethoscope size={22} />
                        </span>

                        <div>
                          <h2>
                            {getDoctorName(appointment)}
                          </h2>

                          <p>
                            {appointment.specialtyName}
                          </p>
                        </div>
                      </div>

                      {appointment.reason && (
                        <div className="patient-appointments-page__detail">
                          <span>Motivo de consulta</span>

                          <p>{appointment.reason}</p>
                        </div>
                      )}

                      {appointment.statusCode === "CANCELADA"
                        && appointment.cancellationReason && (
                          <div className="patient-appointments-page__detail patient-appointments-page__detail--cancelled">
                            <span>Motivo de cancelación</span>

                            <p>
                              {
                                appointment.cancellationReason
                              }
                            </p>
                          </div>
                        )}
                    </AppCard>
                  ),
                )}
              </section>
            )}
          </>
        )}
      </section>
    </main>
  );
}

export default PatientAppointmentsPage;
