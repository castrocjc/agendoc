import {
  useCallback,
  useEffect,
  useMemo,
  useState,
} from "react";
import type {
  FormEvent,
} from "react";

import {
  ArrowLeft,
  Building2,
  CalendarDays,
  CalendarRange,
  Check,
  ChevronRight,
  Circle,
  CircleCheckBig,
  CircleAlert,
  ExternalLink,
  Eye,
  EyeOff,
  Mail,
  MapPin,
  MessageCircle,
  Phone,
  RefreshCw,
  Stethoscope,
  UserRound,
} from "lucide-react";
import {
  useParams,
} from "react-router-dom";

import AppButton from "../../../components/AppButton";
import AppCard from "../../../components/AppCard";
import AppInput from "../../../components/AppInput";
import {
  ApiClientError,
} from "../../../shared/api/apiClient";
import {
  getPublicClinic,
  createFirstAppointment,
  getPublicAvailability,
  getPublicDoctors,
  getPublicSpecialties,
} from "../services/publicReceptionService";
import type {
  FirstAppointmentResponse,
  PublicAgendaAvailability,
  PublicClinic,
  PublicDoctorSummary,
  PublicSpecialty,
} from "../types/publicReception.types";

import "./PublicReceptionPage.css";

type PageStatus =
  | "loading"
  | "success"
  | "not-found"
  | "unavailable"
  | "error";

type ResourceStatus =
  | "idle"
  | "loading"
  | "success"
  | "error";

type BookingStatus =
  | "idle"
  | "submitting"
  | "success"
  | "error";

type BookingStep =
  | "welcome"
  | "specialty"
  | "doctor"
  | "availability"
  | "patient"
  | "confirmation";

interface ProgressStep {
  key: Exclude<BookingStep, "welcome">;
  label: string;
}

interface PatientFormData {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  password: string;
  passwordConfirmation: string;
  reason: string;
}

type PatientFormErrors = Partial<
  Record<keyof PatientFormData, string>
>;

const INITIAL_PATIENT_FORM: PatientFormData = {
  firstName: "",
  lastName: "",
  email: "",
  phone: "",
  password: "",
  passwordConfirmation: "",
  reason: "",
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
    key: "patient",
    label: "Tus datos",
  },
  {
    key: "confirmation",
    label: "Confirmación",
  },
];

function getClinicInitials(publicName: string): string {
  return publicName
    .trim()
    .split(/\s+/)
    .slice(0, 2)
    .map((word) => word.charAt(0).toUpperCase())
    .join("");
}

function normalizeExternalUrl(url: string): string {
  const trimmedUrl = url.trim();

  if (
    trimmedUrl.startsWith("http://") ||
    trimmedUrl.startsWith("https://")
  ) {
    return trimmedUrl;
  }

  return `https://${trimmedUrl}`;
}

function buildPhoneUrl(phone: string): string {
  return `tel:${phone.replace(/[^\d+]/g, "")}`;
}

function buildWhatsAppUrl(whatsapp: string): string {
  return `https://wa.me/${whatsapp.replace(/\D/g, "")}`;
}

function getResourceErrorMessage(
  error: unknown,
  fallbackMessage: string,
): string {
  if (error instanceof ApiClientError) {
    if (error.status === 0) {
      return "No fue posible conectarse con la Recepción Digital.";
    }

    return error.apiMessage ??
      error.message ??
      fallbackMessage;
  }

  return fallbackMessage;
}

function getStepIndex(step: BookingStep): number {
  return PROGRESS_STEPS.findIndex(
    (progressStep) => progressStep.key === step,
  );
}


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


function validatePatientForm(
  form: PatientFormData,
): PatientFormErrors {
  const errors: PatientFormErrors = {};
  const normalizedEmail = form.email.trim();

  if (!form.firstName.trim()) {
    errors.firstName = "Ingresa tus nombres.";
  } else if (form.firstName.trim().length > 100) {
    errors.firstName = "Los nombres no pueden superar 100 caracteres.";
  }

  if (!form.lastName.trim()) {
    errors.lastName = "Ingresa tus apellidos.";
  } else if (form.lastName.trim().length > 100) {
    errors.lastName = "Los apellidos no pueden superar 100 caracteres.";
  }

  if (!normalizedEmail) {
    errors.email = "Ingresa tu correo electrónico.";
  } else if (
    !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(normalizedEmail)
  ) {
    errors.email = "Ingresa un correo electrónico válido.";
  } else if (normalizedEmail.length > 150) {
    errors.email = "El correo no puede superar 150 caracteres.";
  }

  if (!form.phone.trim()) {
    errors.phone = "Ingresa tu número de celular.";
  } else if (form.phone.trim().length > 30) {
    errors.phone = "El celular no puede superar 30 caracteres.";
  }

  if (!form.password) {
    errors.password = "Crea una contraseña.";
  } else if (form.password.length < 8) {
    errors.password = "La contraseña debe tener al menos 8 caracteres.";
  } else if (form.password.length > 100) {
    errors.password = "La contraseña no puede superar 100 caracteres.";
  }

  if (!form.passwordConfirmation) {
    errors.passwordConfirmation = "Confirma tu contraseña.";
  } else if (
    form.passwordConfirmation !== form.password
  ) {
    errors.passwordConfirmation = "Las contraseñas no coinciden.";
  }

  if (form.reason.trim().length > 500) {
    errors.reason = "El motivo no puede superar 500 caracteres.";
  }

  return errors;
}

function PublicReceptionPage() {
  const {
    clinicSlug,
  } = useParams<{
    clinicSlug: string;
  }>();

  const [clinic, setClinic] = useState<PublicClinic | null>(null);
  const [pageStatus, setPageStatus] =
    useState<PageStatus>("loading");
  const [pageError, setPageError] = useState("");

  const [currentStep, setCurrentStep] =
    useState<BookingStep>("welcome");

  const [specialties, setSpecialties] =
    useState<PublicSpecialty[]>([]);
  const [specialtiesStatus, setSpecialtiesStatus] =
    useState<ResourceStatus>("idle");
  const [specialtiesError, setSpecialtiesError] =
    useState("");

  const [selectedSpecialty, setSelectedSpecialty] =
    useState<PublicSpecialty | null>(null);

  const [doctors, setDoctors] =
    useState<PublicDoctorSummary[]>([]);
  const [doctorsStatus, setDoctorsStatus] =
    useState<ResourceStatus>("idle");
  const [doctorsError, setDoctorsError] =
    useState("");

  const [selectedDoctor, setSelectedDoctor] =
    useState<PublicDoctorSummary | null>(null);

  const [availableDates] = useState<string[]>(() =>
    buildAvailableDates(7),
  );
  const [selectedDate, setSelectedDate] =
    useState<string>("");
  const [availability, setAvailability] =
    useState<PublicAgendaAvailability[]>([]);
  const [availabilityStatus, setAvailabilityStatus] =
    useState<ResourceStatus>("idle");
  const [availabilityError, setAvailabilityError] =
    useState("");
  const [selectedAvailability, setSelectedAvailability] =
    useState<PublicAgendaAvailability | null>(null);

  const [patientForm, setPatientForm] =
    useState<PatientFormData>(INITIAL_PATIENT_FORM);
  const [patientFormErrors, setPatientFormErrors] =
    useState<PatientFormErrors>({});
  const [showPassword, setShowPassword] = useState(false);
  const [showPasswordConfirmation, setShowPasswordConfirmation] =
    useState(false);

  const [bookingStatus, setBookingStatus] =
    useState<BookingStatus>("idle");
  const [bookingError, setBookingError] = useState("");
  const [bookingResult, setBookingResult] =
    useState<FirstAppointmentResponse | null>(null);

  const normalizedClinicSlug = useMemo(
    () => clinicSlug?.trim() ?? "",
    [clinicSlug],
  );

  const handleClinicError = useCallback((error: unknown) => {
    setClinic(null);

    if (error instanceof ApiClientError) {
      if (error.status === 404) {
        setPageStatus("not-found");
        setPageError(
          "No encontramos una Recepción Digital asociada a esta dirección.",
        );
        return;
      }

      if (error.status === 403) {
        setPageStatus("unavailable");
        setPageError(
          "La Recepción Digital de este consultorio no se encuentra disponible.",
        );
        return;
      }

      if (error.status === 0) {
        setPageStatus("error");
        setPageError(
          "No fue posible conectarse con la Recepción Digital. Verifica que el servicio se encuentre disponible.",
        );
        return;
      }

      setPageStatus("error");
      setPageError(
        error.apiMessage ??
          error.message ??
          "No fue posible cargar la información del consultorio.",
      );
      return;
    }

    setPageStatus("error");
    setPageError(
      "Ocurrió un error inesperado al cargar la Recepción Digital.",
    );
  }, []);

  const retryLoadClinic = useCallback(() => {
    if (!normalizedClinicSlug) {
      return;
    }

    setClinic(null);
    setPageStatus("loading");
    setPageError("");

    getPublicClinic(normalizedClinicSlug)
      .then((response) => {
        setClinic(response);
        setPageStatus("success");
      })
      .catch(handleClinicError);
  }, [handleClinicError, normalizedClinicSlug]);

  const loadSpecialties = useCallback(() => {
    if (!normalizedClinicSlug) {
      return;
    }

    setSpecialties([]);
    setSpecialtiesStatus("loading");
    setSpecialtiesError("");

    getPublicSpecialties(normalizedClinicSlug)
      .then((response) => {
        setSpecialties(response);
        setSpecialtiesStatus("success");
      })
      .catch((error: unknown) => {
        setSpecialtiesStatus("error");
        setSpecialtiesError(
          getResourceErrorMessage(
            error,
            "No fue posible cargar las especialidades.",
          ),
        );
      });
  }, [normalizedClinicSlug]);

  const startBookingFlow = useCallback(() => {
    setCurrentStep("specialty");

    if (specialtiesStatus === "idle") {
      loadSpecialties();
    }
  }, [loadSpecialties, specialtiesStatus]);

  const selectSpecialty = useCallback((
    specialty: PublicSpecialty,
  ) => {
    if (!normalizedClinicSlug) {
      return;
    }

    setSelectedSpecialty(specialty);
    setSelectedDoctor(null);
    setDoctors([]);
    setDoctorsStatus("loading");
    setDoctorsError("");
    setCurrentStep("doctor");

    getPublicDoctors(
      normalizedClinicSlug,
      specialty.id,
    )
      .then((response) => {
        setDoctors(response);
        setDoctorsStatus("success");
      })
      .catch((error: unknown) => {
        setDoctorsStatus("error");
        setDoctorsError(
          getResourceErrorMessage(
            error,
            "No fue posible cargar los médicos.",
          ),
        );
      });
  }, [normalizedClinicSlug]);

  const retryLoadDoctors = useCallback(() => {
    if (!selectedSpecialty) {
      return;
    }

    setDoctors([]);
    setDoctorsStatus("loading");
    setDoctorsError("");

    getPublicDoctors(
      normalizedClinicSlug,
      selectedSpecialty.id,
    )
      .then((response) => {
        setDoctors(response);
        setDoctorsStatus("success");
      })
      .catch((error: unknown) => {
        setDoctorsStatus("error");
        setDoctorsError(
          getResourceErrorMessage(
            error,
            "No fue posible cargar los médicos.",
          ),
        );
      });
  }, [normalizedClinicSlug, selectedSpecialty]);

  const loadAvailability = useCallback((
    doctorId: number,
    date: string,
  ) => {
    if (!normalizedClinicSlug) {
      return;
    }

    setAvailability([]);
    setSelectedAvailability(null);
    setAvailabilityStatus("loading");
    setAvailabilityError("");

    getPublicAvailability(
      normalizedClinicSlug,
      doctorId,
      date,
    )
      .then((response) => {
        setAvailability(response);
        setAvailabilityStatus("success");
      })
      .catch((error: unknown) => {
        setAvailabilityStatus("error");
        setAvailabilityError(
          getResourceErrorMessage(
            error,
            "No fue posible cargar los horarios disponibles.",
          ),
        );
      });
  }, [normalizedClinicSlug]);

  const selectDoctor = useCallback((
    doctor: PublicDoctorSummary,
  ) => {
    const initialDate = availableDates[0] ?? "";

    setSelectedDoctor(doctor);
    setSelectedDate(initialDate);
    setSelectedAvailability(null);
    setCurrentStep("availability");

    if (initialDate) {
      loadAvailability(
        doctor.id,
        initialDate,
      );
    }
  }, [availableDates, loadAvailability]);

  const selectDate = useCallback((
    date: string,
  ) => {
    if (!selectedDoctor) {
      return;
    }

    setSelectedDate(date);
    setSelectedAvailability(null);

    loadAvailability(
      selectedDoctor.id,
      date,
    );
  }, [loadAvailability, selectedDoctor]);

  const retryLoadAvailability = useCallback(() => {
    if (!selectedDoctor || !selectedDate) {
      return;
    }

    loadAvailability(
      selectedDoctor.id,
      selectedDate,
    );
  }, [
    loadAvailability,
    selectedDate,
    selectedDoctor,
  ]);

  const continueToPatientData = useCallback(() => {
    if (!selectedAvailability) {
      return;
    }

    setCurrentStep("patient");
  }, [selectedAvailability]);

  const updatePatientField = useCallback((
    field: keyof PatientFormData,
    value: string,
  ) => {
    setPatientForm((current) => ({
      ...current,
      [field]: value,
    }));

    setPatientFormErrors((current) => {
      if (!current[field]) {
        return current;
      }

      const nextErrors = {
        ...current,
      };

      delete nextErrors[field];

      return nextErrors;
    });
  }, []);

  const submitPatientForm = useCallback((
    event: FormEvent<HTMLFormElement>,
  ) => {
    event.preventDefault();

    const errors = validatePatientForm(patientForm);

    if (Object.keys(errors).length > 0) {
      setPatientFormErrors(errors);
      return;
    }

    setPatientFormErrors({});
    setCurrentStep("confirmation");
  }, [patientForm]);

  const confirmBooking = useCallback(() => {
    if (
      !selectedDoctor ||
      !selectedAvailability ||
      bookingStatus === "submitting"
    ) {
      return;
    }

    setBookingStatus("submitting");
    setBookingError("");

    createFirstAppointment(
      normalizedClinicSlug,
      {
        firstName: patientForm.firstName.trim(),
        lastName: patientForm.lastName.trim(),
        email: patientForm.email.trim().toLowerCase(),
        phone: patientForm.phone.trim(),
        password: patientForm.password,
        passwordConfirmation:
          patientForm.passwordConfirmation,
        doctorId: selectedDoctor.id,
        agendaBlockId: selectedAvailability.agendaBlockId,
        reason: patientForm.reason.trim() || undefined,
      },
    )
      .then((response) => {
        setBookingResult(response);
        setBookingStatus("success");
      })
      .catch((error: unknown) => {
        setBookingStatus("error");
        setBookingError(
          getResourceErrorMessage(
            error,
            "No fue posible registrar tu cita.",
          ),
        );
      });
  }, [
    bookingStatus,
    normalizedClinicSlug,
    patientForm,
    selectedAvailability,
    selectedDoctor,
  ]);

  const goBack = useCallback(() => {
    if (bookingStatus === "success") {
      return;
    }

    if (currentStep === "confirmation") {
      setBookingStatus("idle");
      setBookingError("");
      setCurrentStep("patient");
      return;
    }

    if (currentStep === "patient") {
      setCurrentStep("availability");
      return;
    }

    if (currentStep === "availability") {
      setSelectedDoctor(null);
      setSelectedDate("");
      setAvailability([]);
      setAvailabilityStatus("idle");
      setAvailabilityError("");
      setSelectedAvailability(null);
      setCurrentStep("doctor");
      return;
    }

    if (currentStep === "doctor") {
      setSelectedSpecialty(null);
      setSelectedDoctor(null);
      setDoctors([]);
      setDoctorsStatus("idle");
      setDoctorsError("");
      setSelectedDate("");
      setAvailability([]);
      setAvailabilityStatus("idle");
      setAvailabilityError("");
      setSelectedAvailability(null);
      setCurrentStep("specialty");
      return;
    }

    setCurrentStep("welcome");
  }, [bookingStatus, currentStep]);

  useEffect(() => {
    if (!normalizedClinicSlug) {
      return;
    }

    let isActive = true;

    getPublicClinic(normalizedClinicSlug)
      .then((response) => {
        if (!isActive) {
          return;
        }

        setClinic(response);
        setPageStatus("success");
      })
      .catch((error: unknown) => {
        if (!isActive) {
          return;
        }

        handleClinicError(error);
      });

    return () => {
      isActive = false;
    };
  }, [handleClinicError, normalizedClinicSlug]);

  if (!normalizedClinicSlug) {
    return (
      <main className="public-reception">
        <AppCard
          className="public-reception__error-card"
          elevation="medium"
        >
          <div
            className="public-reception__error-icon"
            aria-hidden="true"
          >
            <CircleAlert size={30} />
          </div>

          <h1>No pudimos abrir esta Recepción Digital</h1>
          <p>La dirección de la Recepción Digital no es válida.</p>

          <span className="public-reception__powered-by">
            Tecnología de AgenDoc
          </span>
        </AppCard>
      </main>
    );
  }

  if (pageStatus === "loading") {
    return (
      <main className="public-reception">
        <div
          className="public-reception__state"
          role="status"
          aria-live="polite"
        >
          <span
            className="public-reception__loader"
            aria-hidden="true"
          />
          <p>Cargando Recepción Digital...</p>
        </div>
      </main>
    );
  }

  if (pageStatus !== "success" || !clinic) {
    const isUnavailable = pageStatus === "unavailable";

    return (
      <main className="public-reception">
        <AppCard
          className="public-reception__error-card"
          elevation="medium"
        >
          <div
            className="public-reception__error-icon"
            aria-hidden="true"
          >
            <CircleAlert size={30} />
          </div>

          <h1>
            {isUnavailable
              ? "Recepción Digital no disponible"
              : "No pudimos abrir esta Recepción Digital"}
          </h1>

          <p>{pageError}</p>

          {pageStatus === "error" && (
            <AppButton
              type="button"
              fullWidth={false}
              leftIcon={<RefreshCw size={18} />}
              onClick={retryLoadClinic}
            >
              Intentar nuevamente
            </AppButton>
          )}

          <span className="public-reception__powered-by">
            Tecnología de AgenDoc
          </span>
        </AppCard>
      </main>
    );
  }

  const clinicInitials = getClinicInitials(clinic.publicName);
  const currentStepIndex = getStepIndex(currentStep);

  return (
    <main className="public-reception">
      <section className="public-reception__shell">
        <header className="public-reception__header">
          <div className="public-reception__identity">
            {clinic.logoUrl ? (
              <img
                className="public-reception__logo"
                src={clinic.logoUrl}
                alt={`Logo de ${clinic.publicName}`}
              />
            ) : (
              <div
                className="public-reception__logo public-reception__logo--fallback"
                aria-hidden="true"
              >
                {clinicInitials || <Building2 size={30} />}
              </div>
            )}

            <div>
              <span className="public-reception__eyebrow">
                Recepción Digital
              </span>
              <h1>{clinic.publicName}</h1>
            </div>
          </div>

          <span className="public-reception__brand">
            AgenDoc
          </span>
        </header>

        {currentStep === "welcome" ? (
          <section className="public-reception__hero">
            <div className="public-reception__hero-copy">
              <span className="public-reception__hero-icon">
                <CalendarDays size={24} />
              </span>

              <h2>Reserva tu cita médica</h2>

              <p>
                {clinic.publicDescription?.trim() ||
                  "Encuentra la especialidad, el médico y el horario que necesitas para reservar tu primera cita."}
              </p>

              <AppButton
                type="button"
                size="lg"
                className="public-reception__start-button"
                onClick={startBookingFlow}
              >
                Comenzar
              </AppButton>
            </div>

            <AppCard
              className="public-reception__contact-card"
              elevation="medium"
            >
              <h3>Información del consultorio</h3>

              <div className="public-reception__contact-list">
                {clinic.phone && (
                  <a
                    href={buildPhoneUrl(clinic.phone)}
                    className="public-reception__contact-item"
                  >
                    <Phone size={19} />
                    <span>
                      <small>Teléfono</small>
                      {clinic.phone}
                    </span>
                  </a>
                )}

                {clinic.whatsapp && (
                  <a
                    href={buildWhatsAppUrl(clinic.whatsapp)}
                    target="_blank"
                    rel="noreferrer"
                    className="public-reception__contact-item"
                  >
                    <MessageCircle size={19} />
                    <span>
                      <small>WhatsApp</small>
                      {clinic.whatsapp}
                    </span>
                    <ExternalLink size={15} />
                  </a>
                )}

                {clinic.email && (
                  <a
                    href={`mailto:${clinic.email}`}
                    className="public-reception__contact-item"
                  >
                    <Mail size={19} />
                    <span>
                      <small>Correo</small>
                      {clinic.email}
                    </span>
                  </a>
                )}

                {clinic.address && (
                  clinic.mapUrl ? (
                    <a
                      href={normalizeExternalUrl(clinic.mapUrl)}
                      target="_blank"
                      rel="noreferrer"
                      className="public-reception__contact-item"
                    >
                      <MapPin size={19} />
                      <span>
                        <small>Dirección</small>
                        {clinic.address}
                      </span>
                      <ExternalLink size={15} />
                    </a>
                  ) : (
                    <div className="public-reception__contact-item">
                      <MapPin size={19} />
                      <span>
                        <small>Dirección</small>
                        {clinic.address}
                      </span>
                    </div>
                  )
                )}

                {!clinic.phone &&
                  !clinic.whatsapp &&
                  !clinic.email &&
                  !clinic.address && (
                    <p className="public-reception__contact-empty">
                      La información de contacto será proporcionada durante el proceso de reserva.
                    </p>
                  )}
              </div>
            </AppCard>
          </section>
        ) : (
          <section className="public-reception__wizard">
            <nav
              className="public-reception__progress"
              aria-label="Progreso de la reserva"
            >
              {PROGRESS_STEPS.map((progressStep, index) => {
                const isCompleted = index < currentStepIndex;
                const isCurrent = index === currentStepIndex;

                return (
                  <div
                    key={progressStep.key}
                    className={[
                      "public-reception__progress-item",
                      isCompleted
                        ? "public-reception__progress-item--completed"
                        : "",
                      isCurrent
                        ? "public-reception__progress-item--current"
                        : "",
                    ]
                      .filter(Boolean)
                      .join(" ")}
                  >
                    <span className="public-reception__progress-marker">
                      {isCompleted ? (
                        <Check size={15} />
                      ) : (
                        index + 1
                      )}
                    </span>
                    <span>{progressStep.label}</span>
                  </div>
                );
              })}
            </nav>

            <div className="public-reception__wizard-layout">
              <AppCard
                className="public-reception__step-card"
                elevation="medium"
              >
                {bookingStatus !== "success" && (
                  <button
                    type="button"
                    className="public-reception__back-button"
                    onClick={goBack}
                  >
                    <ArrowLeft size={17} />
                    Volver
                  </button>
                )}

                {currentStep === "specialty" && (
                  <>
                    <div className="public-reception__step-heading">
                      <span>Paso 1 de 5</span>
                      <h2>Selecciona una especialidad</h2>
                      <p>
                        Elige el tipo de atención médica que necesitas.
                      </p>
                    </div>

                    {specialtiesStatus === "loading" && (
                      <div
                        className="public-reception__resource-state"
                        role="status"
                        aria-live="polite"
                      >
                        <span
                          className="public-reception__loader"
                          aria-hidden="true"
                        />
                        <p>Cargando especialidades...</p>
                      </div>
                    )}

                    {specialtiesStatus === "error" && (
                      <div className="public-reception__inline-state">
                        <CircleAlert size={26} />
                        <h3>No pudimos cargar las especialidades</h3>
                        <p>{specialtiesError}</p>

                        <AppButton
                          type="button"
                          variant="outline"
                          fullWidth={false}
                          leftIcon={<RefreshCw size={17} />}
                          onClick={loadSpecialties}
                        >
                          Intentar nuevamente
                        </AppButton>
                      </div>
                    )}

                    {specialtiesStatus === "success" &&
                      specialties.length === 0 && (
                        <div className="public-reception__inline-state">
                          <Stethoscope size={28} />
                          <h3>No hay especialidades disponibles</h3>
                          <p>
                            Este consultorio todavía no tiene especialidades habilitadas para reserva pública.
                          </p>
                        </div>
                      )}

                    {specialtiesStatus === "success" &&
                      specialties.length > 0 && (
                        <div className="public-reception__option-grid">
                          {specialties.map((specialty) => (
                            <button
                              key={specialty.id}
                              type="button"
                              className="public-reception__option-card"
                              onClick={() => {
                                selectSpecialty(specialty);
                              }}
                            >
                              <span className="public-reception__option-icon">
                                <Stethoscope size={22} />
                              </span>

                              <span className="public-reception__option-content">
                                <strong>{specialty.name}</strong>
                                <small>Ver médicos disponibles</small>
                              </span>

                              <ChevronRight size={20} />
                            </button>
                          ))}
                        </div>
                      )}
                  </>
                )}

                {currentStep === "doctor" && selectedSpecialty && (
                  <>
                    <div className="public-reception__step-heading">
                      <span>Paso 2 de 5</span>
                      <h2>Selecciona un médico</h2>
                      <p>
                        Médicos disponibles en{" "}
                        <strong>{selectedSpecialty.name}</strong>.
                      </p>
                    </div>

                    {doctorsStatus === "loading" && (
                      <div
                        className="public-reception__resource-state"
                        role="status"
                        aria-live="polite"
                      >
                        <span
                          className="public-reception__loader"
                          aria-hidden="true"
                        />
                        <p>Cargando médicos...</p>
                      </div>
                    )}

                    {doctorsStatus === "error" && (
                      <div className="public-reception__inline-state">
                        <CircleAlert size={26} />
                        <h3>No pudimos cargar los médicos</h3>
                        <p>{doctorsError}</p>

                        <AppButton
                          type="button"
                          variant="outline"
                          fullWidth={false}
                          leftIcon={<RefreshCw size={17} />}
                          onClick={retryLoadDoctors}
                        >
                          Intentar nuevamente
                        </AppButton>
                      </div>
                    )}

                    {doctorsStatus === "success" &&
                      doctors.length === 0 && (
                        <div className="public-reception__inline-state">
                          <UserRound size={28} />
                          <h3>No hay médicos disponibles</h3>
                          <p>
                            No encontramos médicos habilitados para esta especialidad.
                          </p>
                        </div>
                      )}

                    {doctorsStatus === "success" &&
                      doctors.length > 0 && (
                        <div className="public-reception__option-grid">
                          {doctors.map((doctor) => (
                            <button
                              key={doctor.id}
                              type="button"
                              className="public-reception__option-card"
                              onClick={() => {
                                selectDoctor(doctor);
                              }}
                            >
                              <span className="public-reception__option-icon">
                                <UserRound size={22} />
                              </span>

                              <span className="public-reception__option-content">
                                <strong>
                                  Dr. {doctor.firstName} {doctor.lastName}
                                </strong>
                                <small>{doctor.specialtyName}</small>
                              </span>

                              <ChevronRight size={20} />
                            </button>
                          ))}
                        </div>
                      )}
                  </>
                )}

                {currentStep === "availability" &&
                  selectedSpecialty &&
                  selectedDoctor && (
                    <>
                      <div className="public-reception__step-heading">
                        <span>Paso 3 de 5</span>
                        <h2>Selecciona fecha y horario</h2>
                        <p>
                          Disponibilidad de{" "}
                          <strong>
                            Dr. {selectedDoctor.firstName}{" "}
                            {selectedDoctor.lastName}
                          </strong>.
                        </p>
                      </div>

                      <section
                        className="public-reception__availability-section"
                        aria-labelledby="public-reception-date-heading"
                      >
                        <div className="public-reception__section-title">
                          <CalendarRange size={20} />
                          <h3 id="public-reception-date-heading">
                            Selecciona una fecha
                          </h3>
                        </div>

                        <div className="public-reception__date-grid">
                          {availableDates.map((date) => {
                            const label = formatDateLabel(date);
                            const isSelected =
                              selectedDate === date;

                            return (
                              <button
                                key={date}
                                type="button"
                                className={[
                                  "public-reception__date-option",
                                  isSelected
                                    ? "public-reception__date-option--selected"
                                    : "",
                                ]
                                  .filter(Boolean)
                                  .join(" ")}
                                aria-pressed={isSelected}
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

                      <section
                        className="public-reception__availability-section"
                        aria-labelledby="public-reception-time-heading"
                      >
                        <div className="public-reception__section-title">
                          <CalendarDays size={20} />
                          <div>
                            <h3 id="public-reception-time-heading">
                              Selecciona un horario
                            </h3>
                            {selectedDate && (
                              <p>
                                {formatLongDate(selectedDate)}
                              </p>
                            )}
                          </div>
                        </div>

                        {availabilityStatus === "loading" && (
                          <div
                            className="public-reception__resource-state public-reception__resource-state--compact"
                            role="status"
                            aria-live="polite"
                          >
                            <span
                              className="public-reception__loader"
                              aria-hidden="true"
                            />
                            <p>Cargando horarios...</p>
                          </div>
                        )}

                        {availabilityStatus === "error" && (
                          <div className="public-reception__inline-state public-reception__inline-state--compact">
                            <CircleAlert size={26} />
                            <h3>
                              No pudimos cargar los horarios
                            </h3>
                            <p>{availabilityError}</p>

                            <AppButton
                              type="button"
                              variant="outline"
                              fullWidth={false}
                              leftIcon={<RefreshCw size={17} />}
                              onClick={retryLoadAvailability}
                            >
                              Intentar nuevamente
                            </AppButton>
                          </div>
                        )}

                        {availabilityStatus === "success" &&
                          availability.length === 0 && (
                            <div className="public-reception__inline-state public-reception__inline-state--compact">
                              <CalendarDays size={28} />
                              <h3>
                                No hay horarios disponibles
                              </h3>
                              <p>
                                Selecciona otra fecha para revisar nuevas opciones.
                              </p>
                            </div>
                          )}

                        {availabilityStatus === "success" &&
                          availability.length > 0 && (
                            <div className="public-reception__time-grid">
                              {availability.map((slot) => {
                                const isSelected =
                                  selectedAvailability
                                    ?.agendaBlockId ===
                                  slot.agendaBlockId;

                                return (
                                  <button
                                    key={slot.agendaBlockId}
                                    type="button"
                                    className={[
                                      "public-reception__time-option",
                                      isSelected
                                        ? "public-reception__time-option--selected"
                                        : "",
                                    ]
                                      .filter(Boolean)
                                      .join(" ")}
                                    aria-pressed={isSelected}
                                    onClick={() => {
                                      setSelectedAvailability(slot);
                                    }}
                                  >
                                    <strong>
                                      {formatTime(slot.startTime)}
                                    </strong>
                                    <span>
                                      a {formatTime(slot.endTime)}
                                    </span>
                                  </button>
                                );
                              })}
                            </div>
                          )}
                      </section>

                      {selectedAvailability && (
                        <>
                          <div
                            className="public-reception__availability-confirmation"
                            role="status"
                            aria-live="polite"
                          >
                            <CircleCheckBig size={22} />
                            <div>
                              <span>Horario seleccionado</span>
                              <strong>
                                {formatLongDate(
                                  selectedAvailability.appointmentDate,
                                )}
                                {" · "}
                                {formatTime(
                                  selectedAvailability.startTime,
                                )}
                                {" - "}
                                {formatTime(
                                  selectedAvailability.endTime,
                                )}
                              </strong>
                            </div>
                          </div>

                          <div className="public-reception__step-actions">
                            <AppButton
                              type="button"
                              fullWidth={false}
                              rightIcon={<ChevronRight size={18} />}
                              onClick={continueToPatientData}
                            >
                              Continuar con mis datos
                            </AppButton>
                          </div>
                        </>
                      )}
                    </>
                  )}

                {currentStep === "patient" &&
                  selectedSpecialty &&
                  selectedDoctor &&
                  selectedAvailability && (
                    <>
                      <div className="public-reception__step-heading">
                        <span>Paso 4 de 5</span>
                        <h2>Completa tus datos</h2>
                        <p>
                          Crearemos tu acceso como paciente y registraremos tu primera cita.
                        </p>
                      </div>

                      <form
                        className="public-reception__patient-form"
                        noValidate
                        onSubmit={submitPatientForm}
                      >
                        <div className="public-reception__form-grid">
                          <AppInput
                            id="public-first-name"
                            label="Nombres"
                            autoComplete="given-name"
                            value={patientForm.firstName}
                            error={patientFormErrors.firstName}
                            maxLength={100}
                            onChange={(event) => {
                              updatePatientField(
                                "firstName",
                                event.target.value,
                              );
                            }}
                          />

                          <AppInput
                            id="public-last-name"
                            label="Apellidos"
                            autoComplete="family-name"
                            value={patientForm.lastName}
                            error={patientFormErrors.lastName}
                            maxLength={100}
                            onChange={(event) => {
                              updatePatientField(
                                "lastName",
                                event.target.value,
                              );
                            }}
                          />

                          <AppInput
                            id="public-email"
                            label="Correo electrónico"
                            type="email"
                            autoComplete="email"
                            value={patientForm.email}
                            error={patientFormErrors.email}
                            maxLength={150}
                            placeholder="nombre@correo.com"
                            onChange={(event) => {
                              updatePatientField(
                                "email",
                                event.target.value,
                              );
                            }}
                          />

                          <AppInput
                            id="public-phone"
                            label="Celular"
                            type="tel"
                            autoComplete="tel"
                            value={patientForm.phone}
                            error={patientFormErrors.phone}
                            maxLength={30}
                            placeholder="+52 55 1234 5678"
                            onChange={(event) => {
                              updatePatientField(
                                "phone",
                                event.target.value,
                              );
                            }}
                          />

                          <AppInput
                            id="public-password"
                            label="Contraseña"
                            type={showPassword ? "text" : "password"}
                            autoComplete="new-password"
                            value={patientForm.password}
                            error={patientFormErrors.password}
                            helperText="Usa al menos 8 caracteres."
                            maxLength={100}
                            rightIcon={
                              showPassword
                                ? <EyeOff size={18} />
                                : <Eye size={18} />
                            }
                            onChange={(event) => {
                              updatePatientField(
                                "password",
                                event.target.value,
                              );
                            }}
                          />

                          <div className="public-reception__password-field">
                            <AppInput
                              id="public-password-confirmation"
                              label="Confirmar contraseña"
                              type={
                                showPasswordConfirmation
                                  ? "text"
                                  : "password"
                              }
                              autoComplete="new-password"
                              value={patientForm.passwordConfirmation}
                              error={
                                patientFormErrors.passwordConfirmation
                              }
                              maxLength={100}
                              rightIcon={
                                showPasswordConfirmation
                                  ? <EyeOff size={18} />
                                  : <Eye size={18} />
                              }
                              onChange={(event) => {
                                updatePatientField(
                                  "passwordConfirmation",
                                  event.target.value,
                                );
                              }}
                            />
                          </div>
                        </div>

                        <div className="public-reception__password-toggles">
                          <button
                            type="button"
                            onClick={() => {
                              setShowPassword((current) => !current);
                            }}
                          >
                            {showPassword ? "Ocultar" : "Mostrar"} contraseña
                          </button>

                          <button
                            type="button"
                            onClick={() => {
                              setShowPasswordConfirmation(
                                (current) => !current,
                              );
                            }}
                          >
                            {showPasswordConfirmation
                              ? "Ocultar"
                              : "Mostrar"} confirmación
                          </button>
                        </div>

                        <div className="public-reception__textarea-field">
                          <label htmlFor="public-reason">
                            Motivo de la consulta
                            <span>Opcional</span>
                          </label>

                          <textarea
                            id="public-reason"
                            rows={4}
                            maxLength={500}
                            value={patientForm.reason}
                            aria-invalid={Boolean(
                              patientFormErrors.reason,
                            )}
                            aria-describedby={
                              patientFormErrors.reason
                                ? "public-reason-error"
                                : "public-reason-helper"
                            }
                            placeholder="Cuéntanos brevemente el motivo de tu cita."
                            onChange={(event) => {
                              updatePatientField(
                                "reason",
                                event.target.value,
                              );
                            }}
                          />

                          {patientFormErrors.reason ? (
                            <p
                              id="public-reason-error"
                              className="public-reception__field-error"
                            >
                              {patientFormErrors.reason}
                            </p>
                          ) : (
                            <p
                              id="public-reason-helper"
                              className="public-reception__field-helper"
                            >
                              {patientForm.reason.length}/500 caracteres
                            </p>
                          )}
                        </div>

                        <div className="public-reception__privacy-note">
                          <CircleCheckBig size={20} />
                          <p>
                            Tus datos se utilizarán únicamente para crear tu perfil de paciente y gestionar tu cita.
                          </p>
                        </div>

                        <div className="public-reception__step-actions">
                          <AppButton
                            type="submit"
                            fullWidth={false}
                            rightIcon={<ChevronRight size={18} />}
                          >
                            Revisar y confirmar
                          </AppButton>
                        </div>
                      </form>
                    </>
                  )}

                {currentStep === "confirmation" &&
                  selectedSpecialty &&
                  selectedDoctor &&
                  selectedAvailability && (
                    <>
                      {bookingStatus !== "success" && (
                        <>
                          <div className="public-reception__step-heading">
                            <span>Paso 5 de 5</span>
                            <h2>Revisa tu reserva</h2>
                            <p>
                              Confirma que la información sea correcta antes de registrar la cita.
                            </p>
                          </div>

                          <div className="public-reception__confirmation-details">
                            <div className="public-reception__confirmation-row">
                              <span>Paciente</span>
                              <strong>
                                {patientForm.firstName.trim()}{" "}
                                {patientForm.lastName.trim()}
                              </strong>
                            </div>

                            <div className="public-reception__confirmation-row">
                              <span>Correo</span>
                              <strong>
                                {patientForm.email.trim().toLowerCase()}
                              </strong>
                            </div>

                            <div className="public-reception__confirmation-row">
                              <span>Celular</span>
                              <strong>{patientForm.phone.trim()}</strong>
                            </div>

                            <div className="public-reception__confirmation-row">
                              <span>Especialidad</span>
                              <strong>{selectedSpecialty.name}</strong>
                            </div>

                            <div className="public-reception__confirmation-row">
                              <span>Médico</span>
                              <strong>
                                Dr. {selectedDoctor.firstName}{" "}
                                {selectedDoctor.lastName}
                              </strong>
                            </div>

                            <div className="public-reception__confirmation-row">
                              <span>Fecha</span>
                              <strong>
                                {formatLongDate(
                                  selectedAvailability.appointmentDate,
                                )}
                              </strong>
                            </div>

                            <div className="public-reception__confirmation-row">
                              <span>Horario</span>
                              <strong>
                                {formatTime(
                                  selectedAvailability.startTime,
                                )}
                                {" - "}
                                {formatTime(
                                  selectedAvailability.endTime,
                                )}
                              </strong>
                            </div>

                            {patientForm.reason.trim() && (
                              <div className="public-reception__confirmation-row public-reception__confirmation-row--full">
                                <span>Motivo de consulta</span>
                                <strong>{patientForm.reason.trim()}</strong>
                              </div>
                            )}
                          </div>

                          {bookingStatus === "error" && (
                            <div
                              className="public-reception__booking-error"
                              role="alert"
                            >
                              <CircleAlert size={22} />
                              <div>
                                <strong>
                                  No pudimos registrar tu cita
                                </strong>
                                <p>{bookingError}</p>
                              </div>
                            </div>
                          )}

                          <div className="public-reception__step-actions public-reception__step-actions--confirmation">
                            <AppButton
                              type="button"
                              variant="outline"
                              fullWidth={false}
                              disabled={bookingStatus === "submitting"}
                              onClick={goBack}
                            >
                              Modificar mis datos
                            </AppButton>

                            <AppButton
                              type="button"
                              fullWidth={false}
                              isLoading={bookingStatus === "submitting"}
                              leftIcon={<CircleCheckBig size={18} />}
                              onClick={confirmBooking}
                            >
                              Confirmar cita
                            </AppButton>
                          </div>
                        </>
                      )}

                      {bookingStatus === "success" &&
                        bookingResult && (
                          <div
                            className="public-reception__booking-success"
                            role="status"
                            aria-live="polite"
                          >
                            <span className="public-reception__booking-success-icon">
                              <CircleCheckBig size={42} />
                            </span>

                            <div className="public-reception__booking-success-copy">
                              <span>Cita confirmada</span>
                              <h2>
                                Tu primera cita fue registrada correctamente
                              </h2>
                              <p>
                                Enviamos la reserva para{" "}
                                <strong>
                                  {bookingResult.firstName}{" "}
                                  {bookingResult.lastName}
                                </strong>.
                              </p>
                            </div>

                            <div className="public-reception__booking-success-details">
                              <div>
                                <span>Número de cita</span>
                                <strong>
                                  #{bookingResult.appointmentId}
                                </strong>
                              </div>

                              <div>
                                <span>Especialidad</span>
                                <strong>
                                  {bookingResult.specialtyName}
                                </strong>
                              </div>

                              <div>
                                <span>Médico</span>
                                <strong>
                                  Dr. {bookingResult.doctorFirstName}{" "}
                                  {bookingResult.doctorLastName}
                                </strong>
                              </div>

                              <div>
                                <span>Fecha</span>
                                <strong>
                                  {formatLongDate(
                                    bookingResult.appointmentDate,
                                  )}
                                </strong>
                              </div>

                              <div>
                                <span>Horario</span>
                                <strong>
                                  {formatTime(bookingResult.startTime)}
                                  {" - "}
                                  {formatTime(bookingResult.endTime)}
                                </strong>
                              </div>

                              <div>
                                <span>Estado</span>
                                <strong>
                                  {bookingResult.statusName}
                                </strong>
                              </div>
                            </div>

                            <div className="public-reception__booking-success-note">
                              <Mail size={20} />
                              <p>
                                Usa el correo{" "}
                                <strong>{bookingResult.email}</strong>{" "}
                                y la contraseña que acabas de crear para acceder a AgenDoc.
                              </p>
                            </div>
                          </div>
                        )}
                    </>
                  )}
              </AppCard>

              <aside className="public-reception__summary">
                <AppCard
                  className="public-reception__summary-card"
                  elevation="low"
                >
                  <h3>Resumen de tu reserva</h3>

                  <div className="public-reception__summary-item">
                    <span
                      className="public-reception__summary-status public-reception__summary-status--completed"
                      aria-hidden="true"
                    >
                      <CircleCheckBig size={20} />
                    </span>

                    <div className="public-reception__summary-content">
                      <span>Consultorio</span>
                      <strong>{clinic.publicName}</strong>
                    </div>
                  </div>

                  <div className="public-reception__summary-item">
                    <span
                      className={[
                        "public-reception__summary-status",
                        selectedSpecialty
                          ? "public-reception__summary-status--completed"
                          : "public-reception__summary-status--pending",
                      ].join(" ")}
                      aria-hidden="true"
                    >
                      {selectedSpecialty ? (
                        <CircleCheckBig size={20} />
                      ) : (
                        <Circle size={20} />
                      )}
                    </span>

                    <div className="public-reception__summary-content">
                      <span>Especialidad</span>
                      <strong
                        className={
                          selectedSpecialty
                            ? ""
                            : "public-reception__summary-value--pending"
                        }
                      >
                        {selectedSpecialty?.name ||
                          "Selecciona una especialidad"}
                      </strong>
                    </div>
                  </div>

                  <div className="public-reception__summary-item">
                    <span
                      className={[
                        "public-reception__summary-status",
                        selectedDoctor
                          ? "public-reception__summary-status--completed"
                          : "public-reception__summary-status--pending",
                      ].join(" ")}
                      aria-hidden="true"
                    >
                      {selectedDoctor ? (
                        <CircleCheckBig size={20} />
                      ) : (
                        <Circle size={20} />
                      )}
                    </span>

                    <div className="public-reception__summary-content">
                      <span>Médico</span>
                      <strong
                        className={
                          selectedDoctor
                            ? ""
                            : "public-reception__summary-value--pending"
                        }
                      >
                        {selectedDoctor
                          ? `Dr. ${selectedDoctor.firstName} ${selectedDoctor.lastName}`
                          : "Selecciona un médico"}
                      </strong>
                    </div>
                  </div>

                  <div className="public-reception__summary-item">
                    <span
                      className={[
                        "public-reception__summary-status",
                        selectedAvailability
                          ? "public-reception__summary-status--completed"
                          : "public-reception__summary-status--pending",
                      ].join(" ")}
                      aria-hidden="true"
                    >
                      {selectedAvailability ? (
                        <CircleCheckBig size={20} />
                      ) : (
                        <Circle size={20} />
                      )}
                    </span>

                    <div className="public-reception__summary-content">
                      <span>Horario</span>
                      <strong
                        className={
                          selectedAvailability
                            ? ""
                            : "public-reception__summary-value--pending"
                        }
                      >
                        {selectedAvailability
                          ? `${formatLongDate(
                              selectedAvailability.appointmentDate,
                            )} · ${formatTime(
                              selectedAvailability.startTime,
                            )}`
                          : "Selecciona un horario"}
                      </strong>
                    </div>
                  </div>

                  <div className="public-reception__summary-item">
                    <span
                      className={[
                        "public-reception__summary-status",
                        currentStep === "confirmation"
                          ? "public-reception__summary-status--completed"
                          : "public-reception__summary-status--pending",
                      ].join(" ")}
                      aria-hidden="true"
                    >
                      {currentStep === "confirmation" ? (
                        <CircleCheckBig size={20} />
                      ) : (
                        <Circle size={20} />
                      )}
                    </span>

                    <div className="public-reception__summary-content">
                      <span>Datos personales</span>
                      <strong
                        className={
                          currentStep === "confirmation"
                            ? ""
                            : "public-reception__summary-value--pending"
                        }
                      >
                        {currentStep === "confirmation"
                          ? `${patientForm.firstName.trim()} ${patientForm.lastName.trim()}`
                          : "Completa tu información"}
                      </strong>
                    </div>
                  </div>

                  <div className="public-reception__summary-item">
                    <span
                      className={[
                        "public-reception__summary-status",
                        bookingStatus === "success"
                          ? "public-reception__summary-status--completed"
                          : "public-reception__summary-status--pending",
                      ].join(" ")}
                      aria-hidden="true"
                    >
                      {bookingStatus === "success" ? (
                        <CircleCheckBig size={20} />
                      ) : (
                        <Circle size={20} />
                      )}
                    </span>

                    <div className="public-reception__summary-content">
                      <span>Confirmación</span>
                      <strong
                        className={
                          bookingStatus === "success"
                            ? ""
                            : "public-reception__summary-value--pending"
                        }
                      >
                        {bookingStatus === "success"
                          ? "Cita confirmada"
                          : currentStep === "confirmation"
                            ? "Lista para confirmar"
                            : "Pendiente"}
                      </strong>
                    </div>
                  </div>
                </AppCard>
              </aside>
            </div>
          </section>
        )}

        <footer className="public-reception__footer">
          <span>{clinic.publicName}</span>
          <span>Recepción Digital impulsada por AgenDoc</span>
        </footer>
      </section>
    </main>
  );
}

export default PublicReceptionPage;
