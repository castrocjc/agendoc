import {
  useCallback,
  useEffect,
  useMemo,
  useState,
} from "react";
import type {
  ChangeEvent,
  FormEvent,
} from "react";

import {
  ArrowLeft,
  CalendarDays,
  CalendarRange,
  Check,
  Circle,
  CircleAlert,
  CircleCheckBig,
  Clock3,
  Home,
  RefreshCw,
  Stethoscope,
  UserRound,
} from "lucide-react";
import {
  useNavigate,
} from "react-router-dom";

import AppButton from "../../../components/AppButton";
import AppCard from "../../../components/AppCard";
import {
  findAgendaBlocks,
  AgendaServiceError,
} from "../../agenda/services/agendaService";
import type {
  AgendaBlockResponse,
} from "../../agenda/types/agenda.types";
import {
  AppointmentServiceError,
  createPatientAppointment,
} from "../../appointment/services/appointmentService";
import type {
  AppointmentResponse,
} from "../../appointment/types/appointment.types";
import {
  DoctorServiceError,
  getDoctors,
} from "../../doctor/services/doctorService";
import type {
  DoctorResponse,
} from "../../doctor/types/doctor.types";
import type {
  BookingStatus,
  PatientAppointmentFormData,
  PatientAppointmentFormErrors,
  PatientAppointmentStep,
  ResourceStatus,
  SpecialtyOption,
} from "../types/patientAppointment.types";
import {
  validatePatientAppointmentForm,
} from "../validation/patientAppointmentValidation";

import "./PatientAppointmentPage.css";

interface ProgressStep {
  key: PatientAppointmentStep;
  label: string;
}

const INITIAL_FORM: PatientAppointmentFormData = {
  reason: "",
  notes: "",
};

const PROGRESS_STEPS: ProgressStep[] = [
  {
    key: "specialty",
    label: "Especialidad",
  },
  {
    key: "doctor",
    label: "Médico",
  },
  {
    key: "availability",
    label: "Horario",
  },
  {
    key: "confirmation",
    label: "Confirmación",
  },
];

function formatDateForApi(value: Date): string {
  const year = value.getFullYear();
  const month = String(value.getMonth() + 1).padStart(2, "0");
  const day = String(value.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
}

function buildAvailableDates(totalDays: number): string[] {
  const today = new Date();

  return Array.from(
    {
      length: totalDays,
    },
    (_, index) => {
      const date = new Date(today);

      date.setDate(today.getDate() + index);

      return formatDateForApi(date);
    },
  );
}

function formatDateLabel(value: string): {
  weekday: string;
  day: string;
  month: string;
} {
  const date = new Date(`${value}T00:00:00`);

  return {
    weekday: new Intl.DateTimeFormat(
      "es-MX",
      {
        weekday: "short",
      },
    )
      .format(date)
      .replace(".", ""),
    day: new Intl.DateTimeFormat(
      "es-MX",
      {
        day: "2-digit",
      },
    ).format(date),
    month: new Intl.DateTimeFormat(
      "es-MX",
      {
        month: "short",
      },
    )
      .format(date)
      .replace(".", ""),
  };
}

function formatLongDate(value: string): string {
  const date = new Date(`${value}T00:00:00`);

  return new Intl.DateTimeFormat(
    "es-MX",
    {
      weekday: "long",
      day: "numeric",
      month: "long",
      year: "numeric",
    },
  ).format(date);
}

function formatTime(value: string): string {
  return value.slice(0, 5);
}

function getStepIndex(step: PatientAppointmentStep): number {
  return PROGRESS_STEPS.findIndex(
    (progressStep) => progressStep.key === step,
  );
}

function getDoctorName(doctor: DoctorResponse): string {
  return `Dr. ${doctor.firstName} ${doctor.lastName}`;
}

function getSpecialties(
  doctors: DoctorResponse[],
): SpecialtyOption[] {
  const specialties = new Map<number, SpecialtyOption>();

  doctors.forEach((doctor) => {
    specialties.set(
      doctor.specialtyId,
      {
        id: doctor.specialtyId,
        name: doctor.specialtyName,
      },
    );
  });

  return [...specialties.values()].sort(
    (first, second) =>
      first.name.localeCompare(
        second.name,
        "es",
        {
          sensitivity: "base",
        },
      ),
  );
}

function PatientAppointmentPage() {
  const navigate = useNavigate();

  const [currentStep, setCurrentStep] =
    useState<PatientAppointmentStep>("specialty");

  const [doctors, setDoctors] =
    useState<DoctorResponse[]>([]);
  const [doctorsStatus, setDoctorsStatus] =
    useState<ResourceStatus>("loading");
  const [doctorsError, setDoctorsError] =
    useState("");

  const [selectedSpecialty, setSelectedSpecialty] =
    useState<SpecialtyOption | null>(null);
  const [selectedDoctor, setSelectedDoctor] =
    useState<DoctorResponse | null>(null);

  const [availableDates] = useState<string[]>(() =>
    buildAvailableDates(7),
  );
  const [selectedDate, setSelectedDate] =
    useState("");
  const [agendaBlocks, setAgendaBlocks] =
    useState<AgendaBlockResponse[]>([]);
  const [agendaStatus, setAgendaStatus] =
    useState<ResourceStatus>("idle");
  const [agendaError, setAgendaError] =
    useState("");
  const [selectedAgendaBlock, setSelectedAgendaBlock] =
    useState<AgendaBlockResponse | null>(null);

  const [form, setForm] =
    useState<PatientAppointmentFormData>(INITIAL_FORM);
  const [formErrors, setFormErrors] =
    useState<PatientAppointmentFormErrors>({});

  const [bookingStatus, setBookingStatus] =
    useState<BookingStatus>("idle");
  const [bookingError, setBookingError] =
    useState("");
  const [bookingResult, setBookingResult] =
    useState<AppointmentResponse | null>(null);

  const specialties = useMemo(
    () => getSpecialties(doctors),
    [doctors],
  );

  const filteredDoctors = useMemo(() => {
    if (!selectedSpecialty) {
      return [];
    }

    return doctors.filter(
      (doctor) =>
        doctor.specialtyId === selectedSpecialty.id,
    );
  }, [
    doctors,
    selectedSpecialty,
  ]);

  const currentStepIndex = getStepIndex(currentStep);

  const loadDoctors = useCallback(() => {
    setDoctors([]);
    setDoctorsStatus("loading");
    setDoctorsError("");

    getDoctors()
      .then((response) => {
        setDoctors(response);
        setDoctorsStatus("success");
      })
      .catch((error: unknown) => {
        setDoctorsStatus("error");

        if (error instanceof DoctorServiceError) {
          setDoctorsError(error.message);
          return;
        }

        setDoctorsError(
          "No fue posible cargar los médicos disponibles.",
        );
      });
  }, []);

  const loadAgendaBlocks = useCallback((
    doctorId: number,
    appointmentDate: string,
  ) => {
    setAgendaBlocks([]);
    setSelectedAgendaBlock(null);
    setAgendaStatus("loading");
    setAgendaError("");

    findAgendaBlocks(
      doctorId,
      appointmentDate,
    )
      .then((response) => {
        setAgendaBlocks(response);
        setAgendaStatus("success");
      })
      .catch((error: unknown) => {
        setAgendaStatus("error");

        if (error instanceof AgendaServiceError) {
          setAgendaError(error.message);
          return;
        }

        setAgendaError(
          "No fue posible cargar los horarios disponibles.",
        );
      });
  }, []);

  useEffect(() => {
    let isActive = true;

    getDoctors()
      .then((response) => {
        if (!isActive) {
          return;
        }

        setDoctors(response);
        setDoctorsStatus("success");
      })
      .catch((error: unknown) => {
        if (!isActive) {
          return;
        }

        setDoctorsStatus("error");

        if (error instanceof DoctorServiceError) {
          setDoctorsError(error.message);
          return;
        }

        setDoctorsError(
          "No fue posible cargar los médicos disponibles.",
        );
      });

    return () => {
      isActive = false;
    };
  }, []);

  function selectSpecialty(
    specialty: SpecialtyOption,
  ): void {
    setSelectedSpecialty(specialty);
    setSelectedDoctor(null);
    setSelectedDate("");
    setAgendaBlocks([]);
    setAgendaStatus("idle");
    setAgendaError("");
    setSelectedAgendaBlock(null);
    setCurrentStep("doctor");
  }

  function selectDoctor(
    doctor: DoctorResponse,
  ): void {
    const initialDate = availableDates[0] ?? "";

    setSelectedDoctor(doctor);
    setSelectedDate(initialDate);
    setSelectedAgendaBlock(null);
    setCurrentStep("availability");

    if (initialDate) {
      loadAgendaBlocks(
        doctor.id,
        initialDate,
      );
    }
  }

  function selectDate(date: string): void {
    if (!selectedDoctor) {
      return;
    }

    setSelectedDate(date);
    setSelectedAgendaBlock(null);

    loadAgendaBlocks(
      selectedDoctor.id,
      date,
    );
  }

  function continueToConfirmation(): void {
    if (!selectedAgendaBlock) {
      return;
    }

    setCurrentStep("confirmation");
  }

  function updateField(
    event: ChangeEvent<
      HTMLTextAreaElement
    >,
  ): void {
    const field = event.target.name as keyof PatientAppointmentFormData;
    const value = event.target.value;

    setForm((current) => ({
      ...current,
      [field]: value,
    }));

    setFormErrors((current) => {
      if (!current[field]) {
        return current;
      }

      const nextErrors = {
        ...current,
      };

      delete nextErrors[field];

      return nextErrors;
    });
  }

  function submitConfirmation(
    event: FormEvent<HTMLFormElement>,
  ): void {
    event.preventDefault();

    if (
      !selectedDoctor ||
      !selectedAgendaBlock ||
      bookingStatus === "submitting"
    ) {
      return;
    }

    const errors =
      validatePatientAppointmentForm(form);

    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      return;
    }

    setFormErrors({});
    setBookingStatus("submitting");
    setBookingError("");

    createPatientAppointment({
      doctorId: selectedDoctor.id,
      agendaBlockId: selectedAgendaBlock.id,
      reason: form.reason.trim() || null,
      notes: form.notes.trim() || null,
    })
      .then((response) => {
        setBookingResult(response);
        setBookingStatus("success");
      })
      .catch((error: unknown) => {
        setBookingStatus("error");

        if (error instanceof AppointmentServiceError) {
          setBookingError(error.message);
          return;
        }

        setBookingError(
          "No fue posible reservar tu cita.",
        );
      });
  }

  function goBack(): void {
    if (bookingStatus === "success") {
      return;
    }

    setBookingError("");
    setBookingStatus("idle");

    if (currentStep === "confirmation") {
      setCurrentStep("availability");
      return;
    }

    if (currentStep === "availability") {
      setSelectedDoctor(null);
      setSelectedDate("");
      setAgendaBlocks([]);
      setAgendaStatus("idle");
      setAgendaError("");
      setSelectedAgendaBlock(null);
      setCurrentStep("doctor");
      return;
    }

    if (currentStep === "doctor") {
      setSelectedSpecialty(null);
      setSelectedDoctor(null);
      setCurrentStep("specialty");
      return;
    }

    navigate("/account");
  }

  function retryAgenda(): void {
    if (!selectedDoctor || !selectedDate) {
      return;
    }

    loadAgendaBlocks(
      selectedDoctor.id,
      selectedDate,
    );
  }

  return (
    <main className="patient-appointment-page">
      <header className="patient-appointment-page__header">
        <img
          src="/branding/agendoc-logo.png"
          alt="AgenDoc"
          className="patient-appointment-page__logo"
        />

        <AppButton
          type="button"
          variant="ghost"
          fullWidth={false}
          leftIcon={<Home size={18} />}
          onClick={() => {
            navigate("/account");
          }}
        >
          Portal del paciente
        </AppButton>
      </header>

      <section className="patient-appointment-page__content">
        <div className="patient-appointment-page__heading">
          <span
            className="patient-appointment-page__heading-icon"
            aria-hidden="true"
          >
            <CalendarDays size={28} />
          </span>

          <div>
            <p className="patient-appointment-page__eyebrow">
              Portal del paciente
            </p>

            <h1>Reservar una nueva cita</h1>

            <p>
              Selecciona la especialidad, el médico y el horario
              que mejor se adapten a tus necesidades.
            </p>
          </div>
        </div>

        {bookingStatus === "success" && bookingResult ? (
          <AppCard
            className="patient-appointment-page__success-card"
            elevation="medium"
          >
            <span
              className="patient-appointment-page__success-icon"
              aria-hidden="true"
            >
              <CircleCheckBig size={38} />
            </span>

            <p className="patient-appointment-page__eyebrow">
              Reserva completada
            </p>

            <h2>Tu cita fue registrada correctamente</h2>

            <p>
              Conserva la siguiente información para consultar
              posteriormente tu reserva.
            </p>

            <dl className="patient-appointment-page__result-list">
              <div>
                <dt>Número de cita</dt>
                <dd>#{bookingResult.id}</dd>
              </div>

              <div>
                <dt>Médico</dt>
                <dd>
                  Dr. {bookingResult.doctorFirstName}{" "}
                  {bookingResult.doctorLastName}
                </dd>
              </div>

              <div>
                <dt>Fecha</dt>
                <dd>
                  {formatLongDate(
                    bookingResult.appointmentDate,
                  )}
                </dd>
              </div>

              <div>
                <dt>Horario</dt>
                <dd>
                  {formatTime(bookingResult.startTime)} -{" "}
                  {formatTime(bookingResult.endTime)}
                </dd>
              </div>

              <div>
                <dt>Estado</dt>
                <dd>{bookingResult.statusName}</dd>
              </div>
            </dl>

            <AppButton
              type="button"
              fullWidth={false}
              leftIcon={<Home size={18} />}
              onClick={() => {
                navigate("/account");
              }}
            >
              Volver al portal
            </AppButton>
          </AppCard>
        ) : (
          <>
            <nav
              className="patient-appointment-page__progress"
              aria-label="Progreso de la reserva"
            >
              {PROGRESS_STEPS.map(
                (progressStep, index) => {
                  const isCompleted =
                    index < currentStepIndex;
                  const isCurrent =
                    index === currentStepIndex;

                  return (
                    <div
                      key={progressStep.key}
                      className={[
                        "patient-appointment-page__progress-item",
                        isCompleted
                          ? "patient-appointment-page__progress-item--completed"
                          : "",
                        isCurrent
                          ? "patient-appointment-page__progress-item--current"
                          : "",
                      ]
                        .filter(Boolean)
                        .join(" ")}
                    >
                      <span className="patient-appointment-page__progress-marker">
                        {isCompleted ? (
                          <Check size={15} />
                        ) : (
                          index + 1
                        )}
                      </span>

                      <span>{progressStep.label}</span>
                    </div>
                  );
                },
              )}
            </nav>

            <div className="patient-appointment-page__layout">
              <AppCard
                className="patient-appointment-page__step-card"
                elevation="medium"
              >
                <button
                  type="button"
                  className="patient-appointment-page__back-button"
                  onClick={goBack}
                >
                  <ArrowLeft size={17} />
                  Volver
                </button>

                {currentStep === "specialty" && (
                  <>
                    <div className="patient-appointment-page__step-heading">
                      <span>Paso 1 de 4</span>
                      <h2>Selecciona una especialidad</h2>
                      <p>
                        Elige el tipo de atención médica que
                        necesitas.
                      </p>
                    </div>

                    {doctorsStatus === "loading" && (
                      <div
                        className="patient-appointment-page__state"
                        role="status"
                      >
                        <span className="patient-appointment-page__loader" />
                        <p>Cargando especialidades...</p>
                      </div>
                    )}

                    {doctorsStatus === "error" && (
                      <div className="patient-appointment-page__state patient-appointment-page__state--error">
                        <CircleAlert size={28} />
                        <h3>No pudimos cargar la información</h3>
                        <p>{doctorsError}</p>

                        <AppButton
                          type="button"
                          variant="outline"
                          fullWidth={false}
                          leftIcon={<RefreshCw size={17} />}
                          onClick={loadDoctors}
                        >
                          Intentar nuevamente
                        </AppButton>
                      </div>
                    )}

                    {doctorsStatus === "success" &&
                      specialties.length === 0 && (
                        <div className="patient-appointment-page__state">
                          <Stethoscope size={29} />
                          <h3>No hay especialidades disponibles</h3>
                          <p>
                            El consultorio todavía no tiene médicos
                            disponibles para reservar.
                          </p>
                        </div>
                      )}

                    {doctorsStatus === "success" &&
                      specialties.length > 0 && (
                        <div className="patient-appointment-page__option-grid">
                          {specialties.map((specialty) => (
                            <button
                              key={specialty.id}
                              type="button"
                              className="patient-appointment-page__option-card"
                              onClick={() => {
                                selectSpecialty(specialty);
                              }}
                            >
                              <span className="patient-appointment-page__option-icon">
                                <Stethoscope size={22} />
                              </span>

                              <span className="patient-appointment-page__option-content">
                                <strong>{specialty.name}</strong>
                                <small>
                                  Ver médicos disponibles
                                </small>
                              </span>
                            </button>
                          ))}
                        </div>
                      )}
                  </>
                )}

                {currentStep === "doctor" &&
                  selectedSpecialty && (
                    <>
                      <div className="patient-appointment-page__step-heading">
                        <span>Paso 2 de 4</span>
                        <h2>Selecciona un médico</h2>
                        <p>
                          Médicos disponibles en{" "}
                          <strong>
                            {selectedSpecialty.name}
                          </strong>.
                        </p>
                      </div>

                      {filteredDoctors.length === 0 ? (
                        <div className="patient-appointment-page__state">
                          <UserRound size={29} />
                          <h3>No hay médicos disponibles</h3>
                          <p>
                            No encontramos médicos activos para
                            esta especialidad.
                          </p>
                        </div>
                      ) : (
                        <div className="patient-appointment-page__option-grid">
                          {filteredDoctors.map((doctor) => (
                            <button
                              key={doctor.id}
                              type="button"
                              className="patient-appointment-page__option-card"
                              onClick={() => {
                                selectDoctor(doctor);
                              }}
                            >
                              <span className="patient-appointment-page__option-icon">
                                <UserRound size={22} />
                              </span>

                              <span className="patient-appointment-page__option-content">
                                <strong>
                                  {getDoctorName(doctor)}
                                </strong>
                                <small>
                                  {doctor.specialtyName}
                                </small>
                              </span>
                            </button>
                          ))}
                        </div>
                      )}
                    </>
                  )}

                {currentStep === "availability" &&
                  selectedDoctor &&
                  selectedSpecialty && (
                    <>
                      <div className="patient-appointment-page__step-heading">
                        <span>Paso 3 de 4</span>
                        <h2>Selecciona fecha y horario</h2>
                        <p>
                          Disponibilidad de{" "}
                          <strong>
                            {getDoctorName(selectedDoctor)}
                          </strong>.
                        </p>
                      </div>

                      <section className="patient-appointment-page__availability-section">
                        <div className="patient-appointment-page__section-title">
                          <CalendarRange size={20} />
                          <h3>Selecciona una fecha</h3>
                        </div>

                        <div className="patient-appointment-page__date-grid">
                          {availableDates.map((date) => {
                            const label =
                              formatDateLabel(date);
                            const isSelected =
                              selectedDate === date;

                            return (
                              <button
                                key={date}
                                type="button"
                                aria-pressed={isSelected}
                                className={[
                                  "patient-appointment-page__date-option",
                                  isSelected
                                    ? "patient-appointment-page__date-option--selected"
                                    : "",
                                ]
                                  .filter(Boolean)
                                  .join(" ")}
                                onClick={() => {
                                  selectDate(date);
                                }}
                              >
                                <span>{label.weekday}</span>
                                <strong>{label.day}</strong>
                                <small>{label.month}</small>
                              </button>
                            );
                          })}
                        </div>
                      </section>

                      <section className="patient-appointment-page__availability-section">
                        <div className="patient-appointment-page__section-title">
                          <Clock3 size={20} />

                          <div>
                            <h3>Selecciona un horario</h3>

                            {selectedDate && (
                              <p>
                                {formatLongDate(selectedDate)}
                              </p>
                            )}
                          </div>
                        </div>

                        {agendaStatus === "loading" && (
                          <div
                            className="patient-appointment-page__state patient-appointment-page__state--compact"
                            role="status"
                          >
                            <span className="patient-appointment-page__loader" />
                            <p>Cargando horarios...</p>
                          </div>
                        )}

                        {agendaStatus === "error" && (
                          <div className="patient-appointment-page__state patient-appointment-page__state--error patient-appointment-page__state--compact">
                            <CircleAlert size={27} />
                            <h3>
                              No pudimos cargar los horarios
                            </h3>
                            <p>{agendaError}</p>

                            <AppButton
                              type="button"
                              variant="outline"
                              fullWidth={false}
                              leftIcon={<RefreshCw size={17} />}
                              onClick={retryAgenda}
                            >
                              Intentar nuevamente
                            </AppButton>
                          </div>
                        )}

                        {agendaStatus === "success" &&
                          agendaBlocks.length === 0 && (
                            <div className="patient-appointment-page__state patient-appointment-page__state--compact">
                              <CalendarDays size={29} />
                              <h3>
                                No hay horarios disponibles
                              </h3>
                              <p>
                                Selecciona otra fecha para revisar
                                nuevas opciones.
                              </p>
                            </div>
                          )}

                        {agendaStatus === "success" &&
                          agendaBlocks.length > 0 && (
                            <div className="patient-appointment-page__time-grid">
                              {agendaBlocks.map((block) => {
                                const isSelected =
                                  selectedAgendaBlock?.id ===
                                  block.id;

                                return (
                                  <button
                                    key={block.id}
                                    type="button"
                                    aria-pressed={isSelected}
                                    className={[
                                      "patient-appointment-page__time-option",
                                      isSelected
                                        ? "patient-appointment-page__time-option--selected"
                                        : "",
                                    ]
                                      .filter(Boolean)
                                      .join(" ")}
                                    onClick={() => {
                                      setSelectedAgendaBlock(
                                        block,
                                      );
                                    }}
                                  >
                                    {formatTime(block.startTime)}
                                    <small>
                                      hasta{" "}
                                      {formatTime(block.endTime)}
                                    </small>
                                  </button>
                                );
                              })}
                            </div>
                          )}
                      </section>

                      {selectedAgendaBlock && (
                        <div
                          className="patient-appointment-page__availability-confirmation"
                          role="status"
                          aria-live="polite"
                        >
                          <CircleCheckBig size={22} />

                          <div>
                            <span>Horario seleccionado</span>

                            <strong>
                              {formatLongDate(
                                selectedAgendaBlock.appointmentDate,
                              )}
                              {" · "}
                              {formatTime(
                                selectedAgendaBlock.startTime,
                              )}
                              {" - "}
                              {formatTime(
                                selectedAgendaBlock.endTime,
                              )}
                            </strong>
                          </div>
                        </div>
                      )}

                      <div className="patient-appointment-page__actions">
                        <AppButton
                          type="button"
                          disabled={!selectedAgendaBlock}
                          onClick={continueToConfirmation}
                        >
                          Continuar
                        </AppButton>
                      </div>
                    </>
                  )}

                {currentStep === "confirmation" &&
                  selectedDoctor &&
                  selectedSpecialty &&
                  selectedAgendaBlock && (
                    <form
                      className="patient-appointment-page__confirmation"
                      onSubmit={submitConfirmation}
                    >
                      <div className="patient-appointment-page__step-heading">
                        <span>Paso 4 de 4</span>
                        <h2>Confirma tu cita</h2>
                        <p>
                          Revisa los datos antes de registrar la
                          reserva.
                        </p>
                      </div>

                      <dl className="patient-appointment-page__summary-list">
                        <div>
                          <dt>Especialidad</dt>
                          <dd>
                            {selectedSpecialty.name}
                          </dd>
                        </div>

                        <div>
                          <dt>Médico</dt>
                          <dd>
                            {getDoctorName(selectedDoctor)}
                          </dd>
                        </div>

                        <div>
                          <dt>Fecha</dt>
                          <dd>
                            {formatLongDate(
                              selectedAgendaBlock.appointmentDate,
                            )}
                          </dd>
                        </div>

                        <div>
                          <dt>Horario</dt>
                          <dd>
                            {formatTime(
                              selectedAgendaBlock.startTime,
                            )}{" "}
                            -{" "}
                            {formatTime(
                              selectedAgendaBlock.endTime,
                            )}
                          </dd>
                        </div>
                      </dl>

                      <div className="patient-appointment-page__form-fields">
                        <label>
                          <span>Motivo de la consulta</span>
                          <small>Opcional, máximo 500 caracteres</small>

                          <textarea
                            name="reason"
                            rows={3}
                            maxLength={500}
                            value={form.reason}
                            aria-invalid={Boolean(formErrors.reason)}
                            onChange={updateField}
                          />

                          <small className="patient-appointment-page__character-counter">
                            {form.reason.length}/500 caracteres
                          </small>

                          {formErrors.reason && (
                            <small className="patient-appointment-page__field-error">
                              {formErrors.reason}
                            </small>
                          )}
                        </label>

                        <label>
                          <span>Observaciones adicionales</span>
                          <small>
                            Opcional, máximo 1000 caracteres
                          </small>

                          <textarea
                            name="notes"
                            rows={4}
                            maxLength={1000}
                            value={form.notes}
                            aria-invalid={Boolean(formErrors.notes)}
                            onChange={updateField}
                          />

                          <small className="patient-appointment-page__character-counter">
                            {form.notes.length}/1000 caracteres
                          </small>

                          {formErrors.notes && (
                            <small className="patient-appointment-page__field-error">
                              {formErrors.notes}
                            </small>
                          )}
                        </label>
                      </div>

                      {bookingStatus === "error" && (
                        <div
                          className="patient-appointment-page__booking-error"
                          role="alert"
                        >
                          <CircleAlert size={22} />
                          <p>{bookingError}</p>
                        </div>
                      )}

                      <div className="patient-appointment-page__actions">
                        <AppButton
                          type="submit"
                          isLoading={
                            bookingStatus === "submitting"
                          }
                          disabled={
                            bookingStatus === "submitting"
                          }
                          leftIcon={
                            <CircleCheckBig size={18} />
                          }
                        >
                          Confirmar reserva
                        </AppButton>
                      </div>
                    </form>
                  )}
              </AppCard>

              <aside className="patient-appointment-page__summary">
                <AppCard
                  className="patient-appointment-page__summary-card"
                  elevation="low"
                >
                  <h3>Resumen de tu cita</h3>

                  <div className="patient-appointment-page__summary-item">
                    <span
                      className={[
                        "patient-appointment-page__summary-status",
                        selectedSpecialty
                          ? "patient-appointment-page__summary-status--completed"
                          : "patient-appointment-page__summary-status--pending",
                      ].join(" ")}
                      aria-hidden="true"
                    >
                      {selectedSpecialty ? (
                        <CircleCheckBig size={20} />
                      ) : (
                        <Circle size={20} />
                      )}
                    </span>

                    <div className="patient-appointment-page__summary-content">
                      <span>Especialidad</span>

                      <strong
                        className={
                          selectedSpecialty
                            ? ""
                            : "patient-appointment-page__summary-value--pending"
                        }
                      >
                        {selectedSpecialty?.name ??
                          "Selecciona una especialidad"}
                      </strong>
                    </div>
                  </div>

                  <div className="patient-appointment-page__summary-item">
                    <span
                      className={[
                        "patient-appointment-page__summary-status",
                        selectedDoctor
                          ? "patient-appointment-page__summary-status--completed"
                          : "patient-appointment-page__summary-status--pending",
                      ].join(" ")}
                      aria-hidden="true"
                    >
                      {selectedDoctor ? (
                        <CircleCheckBig size={20} />
                      ) : (
                        <Circle size={20} />
                      )}
                    </span>

                    <div className="patient-appointment-page__summary-content">
                      <span>Médico</span>

                      <strong
                        className={
                          selectedDoctor
                            ? ""
                            : "patient-appointment-page__summary-value--pending"
                        }
                      >
                        {selectedDoctor
                          ? getDoctorName(selectedDoctor)
                          : "Selecciona un médico"}
                      </strong>
                    </div>
                  </div>

                  <div className="patient-appointment-page__summary-item">
                    <span
                      className={[
                        "patient-appointment-page__summary-status",
                        selectedAgendaBlock
                          ? "patient-appointment-page__summary-status--completed"
                          : "patient-appointment-page__summary-status--pending",
                      ].join(" ")}
                      aria-hidden="true"
                    >
                      {selectedAgendaBlock ? (
                        <CircleCheckBig size={20} />
                      ) : (
                        <Circle size={20} />
                      )}
                    </span>

                    <div className="patient-appointment-page__summary-content">
                      <span>Horario</span>

                      <strong
                        className={
                          selectedAgendaBlock
                            ? ""
                            : "patient-appointment-page__summary-value--pending"
                        }
                      >
                        {selectedAgendaBlock
                          ? `${formatLongDate(
                              selectedAgendaBlock.appointmentDate,
                            )} · ${formatTime(
                              selectedAgendaBlock.startTime,
                            )}`
                          : "Selecciona un horario"}
                      </strong>
                    </div>
                  </div>

                  <div className="patient-appointment-page__summary-item">
                    <span
                      className={[
                        "patient-appointment-page__summary-status",
                        currentStep === "confirmation"
                          ? "patient-appointment-page__summary-status--completed"
                          : "patient-appointment-page__summary-status--pending",
                      ].join(" ")}
                      aria-hidden="true"
                    >
                      {currentStep === "confirmation" ? (
                        <CircleCheckBig size={20} />
                      ) : (
                        <Circle size={20} />
                      )}
                    </span>

                    <div className="patient-appointment-page__summary-content">
                      <span>Confirmación</span>

                      <strong
                        className={
                          currentStep === "confirmation"
                            ? ""
                            : "patient-appointment-page__summary-value--pending"
                        }
                      >
                        {currentStep === "confirmation"
                          ? "Lista para confirmar"
                          : "Pendiente"}
                      </strong>
                    </div>
                  </div>
                </AppCard>
              </aside>
            </div>
          </>
        )}
      </section>
    </main>
  );
}

export default PatientAppointmentPage;
