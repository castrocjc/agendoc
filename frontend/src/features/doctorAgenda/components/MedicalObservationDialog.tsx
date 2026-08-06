import {
  useCallback,
  useEffect,
  useId,
  useState,
} from "react";

import {
  CalendarDays,
  CheckCircle2,
  Clock3,
  FileText,
  Save,
  UserRound,
  X,
} from "lucide-react";

import AppButton from "../../../components/AppButton";
import {
  AppointmentServiceError,
  findMedicalObservation,
  markAppointmentAsAttended,
  registerMedicalObservation,
} from "../../appointment/services/appointmentService";
import type {
  AppointmentAgendaResponse,
} from "../../appointment/types/appointment.types";

import "./MedicalObservationDialog.css";

const MAX_OBSERVATION_LENGTH = 2000;

interface MedicalObservationDialogProps {
  appointment: AppointmentAgendaResponse | null;
  open: boolean;
  onClose: () => void;
  onSaved?: () => void;
  onAttended?: () => void;
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

function MedicalObservationDialog({
  appointment,
  open,
  onClose,
  onSaved,
  onAttended,
}: MedicalObservationDialogProps) {
  const titleId = useId();
  const descriptionId = useId();

  const [observation, setObservation] = useState("");
  const [recordedAt, setRecordedAt] = useState<string | null>(null);

  const [isLoading, setIsLoading] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isCompleting, setIsCompleting] = useState(false);
  const [
    showAttendanceConfirmation,
    setShowAttendanceConfirmation,
  ] = useState(false);

  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [validationMessage, setValidationMessage] = useState("");

  const handleClose = useCallback((): void => {
    if (isSubmitting || isCompleting) {
      return;
    }

    setObservation("");
    setRecordedAt(null);
    setIsLoading(true);
    setErrorMessage("");
    setSuccessMessage("");
    setValidationMessage("");
    setShowAttendanceConfirmation(false);

    onClose();
  }, [
    isCompleting,
    isSubmitting,
    onClose,
  ]);

  useEffect(() => {
    if (!open || !appointment) {
      return undefined;
    }

    let isMounted = true;

    findMedicalObservation(appointment.id)
      .then((response) => {
        if (!isMounted) {
          return;
        }

        setObservation(response.observation ?? "");
        setRecordedAt(response.recordedAt);
      })
      .catch((error: unknown) => {
        if (!isMounted) {
          return;
        }

        if (error instanceof AppointmentServiceError) {
          setErrorMessage(error.message);
        } else {
          setErrorMessage(
            "No fue posible consultar la observación médica. Inténtalo nuevamente.",
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
  }, [
    appointment,
    open,
  ]);

  useEffect(() => {
    if (!open) {
      return undefined;
    }

    const handleKeyDown = (
      event: KeyboardEvent,
    ): void => {
      if (event.key === "Escape") {
        handleClose();
      }
    };

    document.addEventListener(
      "keydown",
      handleKeyDown,
    );

    const previousOverflow =
      document.body.style.overflow;

    document.body.style.overflow = "hidden";

    return () => {
      document.removeEventListener(
        "keydown",
        handleKeyDown,
      );

      document.body.style.overflow =
        previousOverflow;
    };
  }, [
    handleClose,
    open,
  ]);

  if (!open || !appointment) {
    return null;
  }

  const normalizedObservation =
    observation.trim();

  async function handleSubmit(): Promise<void> {
    if (!appointment) {
      return;
    }

    setErrorMessage("");
    setSuccessMessage("");
    setValidationMessage("");

    if (!normalizedObservation) {
      setValidationMessage(
        "La observación médica es obligatoria.",
      );

      return;
    }

    if (
      normalizedObservation.length
      > MAX_OBSERVATION_LENGTH
    ) {
      setValidationMessage(
        "La observación médica no puede superar los 2000 caracteres.",
      );

      return;
    }

    setIsSubmitting(true);

    try {
      const response =
        await registerMedicalObservation(
          appointment.id,
          {
            observation: normalizedObservation,
          },
        );

      setObservation(response.observation ?? "");
      setRecordedAt(response.recordedAt);

      setSuccessMessage(
        "La observación médica fue guardada correctamente.",
      );

      onSaved?.();
    } catch (error) {
      if (error instanceof AppointmentServiceError) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage(
          "No fue posible guardar la observación médica. Inténtalo nuevamente.",
        );
      }
    } finally {
      setIsSubmitting(false);
    }
  }

  async function handleMarkAsAttended(): Promise<void> {
    if (!appointment) {
      return;
    }

    setErrorMessage("");
    setSuccessMessage("");
    setValidationMessage("");
    setIsCompleting(true);

    try {
      await markAppointmentAsAttended(
        appointment.id,
      );

      setShowAttendanceConfirmation(false);

      onAttended?.();
    } catch (error) {
      if (error instanceof AppointmentServiceError) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage(
          "No fue posible marcar la cita como atendida. Inténtalo nuevamente.",
        );
      }
    } finally {
      setIsCompleting(false);
    }
  }

  const operationInProgress =
    isSubmitting || isCompleting;

  const canMarkAsAttended =
    appointment.statusCode === "CONFIRMADA";

  return (
    <div
      className="medical-observation-dialog"
      role="presentation"
      onMouseDown={(event) => {
        if (event.target === event.currentTarget) {
          handleClose();
        }
      }}
    >
      <section
        className="medical-observation-dialog__panel"
        role="dialog"
        aria-modal="true"
        aria-labelledby={titleId}
        aria-describedby={descriptionId}
      >
        <button
          type="button"
          className="medical-observation-dialog__close"
          aria-label="Cerrar diálogo"
          disabled={operationInProgress}
          onClick={handleClose}
        >
          <X size={20} />
        </button>

        <span
          className="medical-observation-dialog__icon"
          aria-hidden="true"
        >
          <FileText size={28} />
        </span>

        <h2 id={titleId}>
          Observación médica
        </h2>

        <p
          id={descriptionId}
          className="medical-observation-dialog__description"
        >
          Registra una observación básica sobre la atención del paciente.
        </p>

        <div className="medical-observation-dialog__summary">
          <div>
            <UserRound size={18} />

            <span>
              {appointment.patientFirstName}
              {" "}
              {appointment.patientLastName}
            </span>
          </div>

          <div>
            <CalendarDays size={18} />

            <span>
              {formatLongDate(
                appointment.appointmentDate,
              )}
            </span>
          </div>

          <div>
            <Clock3 size={18} />

            <span>
              {formatTime(
                appointment.startTime,
              )}
              {" - "}
              {formatTime(
                appointment.endTime,
              )}
            </span>
          </div>
        </div>

        {isLoading ? (
          <div className="medical-observation-dialog__loading">
            <span className="medical-observation-dialog__spinner" />

            <p>
              Consultando la observación médica...
            </p>
          </div>
        ) : (
          <>
            <label className="medical-observation-dialog__field">
              <span>
                Observación
                <small>Obligatoria</small>
              </span>

              <textarea
                value={observation}
                maxLength={MAX_OBSERVATION_LENGTH}
                rows={8}
                disabled={operationInProgress}
                placeholder="Describe brevemente la evaluación, evolución o indicaciones relevantes para la atención."
                aria-invalid={Boolean(
                  validationMessage,
                )}
                onChange={(event) => {
                  setObservation(event.target.value);
                  setValidationMessage("");
                  setSuccessMessage("");
                }}
              />
            </label>

            <div className="medical-observation-dialog__field-meta">
              <span>
                {recordedAt
                  ? "Esta observación ya fue registrada anteriormente."
                  : "Aún no existe una observación registrada."}
              </span>

              <span>
                {observation.length}
                {" / "}
                {MAX_OBSERVATION_LENGTH}
              </span>
            </div>

            {validationMessage && (
              <p
                className="medical-observation-dialog__error"
                role="alert"
              >
                {validationMessage}
              </p>
            )}

            {errorMessage && (
              <p
                className="medical-observation-dialog__error"
                role="alert"
              >
                {errorMessage}
              </p>
            )}

            {successMessage && (
              <p
                className="medical-observation-dialog__success"
                role="status"
              >
                {successMessage}
              </p>
            )}

            {showAttendanceConfirmation && (
              <div
                className="medical-observation-dialog__confirmation"
                role="alert"
              >
                <strong>
                  ¿Confirmar atención?
                </strong>

                <p>
                  La cita cambiará al estado Atendida. Verifica que la observación médica esté guardada antes de continuar.
                </p>

                <div className="medical-observation-dialog__confirmation-actions">
                  <AppButton
                    type="button"
                    variant="ghost"
                    fullWidth={false}
                    disabled={operationInProgress}
                    onClick={() => {
                      setShowAttendanceConfirmation(false);
                    }}
                  >
                    Volver
                  </AppButton>

                  <AppButton
                    type="button"
                    fullWidth={false}
                    leftIcon={<CheckCircle2 size={17} />}
                    isLoading={isCompleting}
                    disabled={isSubmitting}
                    onClick={() => {
                      void handleMarkAsAttended();
                    }}
                  >
                    Confirmar atención
                  </AppButton>
                </div>
              </div>
            )}

            {!showAttendanceConfirmation && (
              <div className="medical-observation-dialog__actions">
                <AppButton
                  type="button"
                  variant="ghost"
                  fullWidth={false}
                  disabled={operationInProgress}
                  onClick={handleClose}
                >
                  Cerrar
                </AppButton>

                <div className="medical-observation-dialog__primary-actions">
                  {canMarkAsAttended && (
                    <AppButton
                      type="button"
                      variant="secondary"
                      fullWidth={false}
                      leftIcon={<CheckCircle2 size={17} />}
                      disabled={
                        operationInProgress
                        || Boolean(errorMessage)
                      }
                      onClick={() => {
                        setErrorMessage("");
                        setSuccessMessage("");
                        setValidationMessage("");
                        setShowAttendanceConfirmation(true);
                      }}
                    >
                      Marcar como atendida
                    </AppButton>
                  )}

                  <AppButton
                    type="button"
                    fullWidth={false}
                    leftIcon={<Save size={17} />}
                    isLoading={isSubmitting}
                    disabled={
                      isCompleting
                      || Boolean(errorMessage)
                    }
                    onClick={() => {
                      void handleSubmit();
                    }}
                  >
                    Guardar observación
                  </AppButton>
                </div>
              </div>
            )}
          </>
        )}
      </section>
    </div>
  );
}

export default MedicalObservationDialog;
