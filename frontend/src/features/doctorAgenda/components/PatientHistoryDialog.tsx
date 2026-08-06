import {
  useCallback,
  useEffect,
  useId,
  useState,
} from "react";

import {
  CalendarDays,
  Clock3,
  FileClock,
  Info,
  Stethoscope,
  UserRound,
  X,
} from "lucide-react";

import AppButton from "../../../components/AppButton";
import {
  AppointmentServiceError,
  findPatientMedicalHistory,
} from "../../appointment/services/appointmentService";
import type {
  AppointmentAgendaResponse,
  PatientMedicalHistoryResponse,
} from "../../appointment/types/appointment.types";

import "./PatientHistoryDialog.css";

interface PatientHistoryDialogProps {
  appointment: AppointmentAgendaResponse | null;
  open: boolean;
  onClose: () => void;
}

function formatLongDate(value: string): string {
  const date = new Date(`${value}T00:00:00`);

  const formattedDate = new Intl.DateTimeFormat(
    "es-MX",
    {
      day: "numeric",
      month: "short",
      year: "numeric",
    },
  ).format(date);

  return formattedDate
    .replace(".", "")
    .toUpperCase();
}

function formatTime(value: string): string {
  return value.slice(0, 5);
}

function formatRecordedAt(
  value: string | null,
): string | null {
  if (!value) {
    return null;
  }

  const date = new Date(value);

  if (Number.isNaN(date.getTime())) {
    return null;
  }

  return new Intl.DateTimeFormat(
    "es-MX",
    {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    },
  ).format(date);
}

function PatientHistoryDialog({
  appointment,
  open,
  onClose,
}: PatientHistoryDialogProps) {
  const titleId = useId();
  const descriptionId = useId();

  const [history, setHistory] = useState<
    PatientMedicalHistoryResponse[]
  >([]);

  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const handleClose = useCallback((): void => {
    setHistory([]);
    setIsLoading(true);
    setErrorMessage("");

    onClose();
  }, [
    onClose,
  ]);

  useEffect(() => {
    if (!open || !appointment) {
      return undefined;
    }

    let isMounted = true;

    const appointmentId = appointment.id;

    async function loadHistory(): Promise<void> {
      try {
        const response =
          await findPatientMedicalHistory(
            appointmentId,
          );

        if (!isMounted) {
          return;
        }

        setHistory(response);
        setErrorMessage("");
      } catch (error) {
        if (!isMounted) {
          return;
        }

        setHistory([]);

        if (error instanceof AppointmentServiceError) {
          setErrorMessage(error.message);
        } else {
          setErrorMessage(
            "No fue posible consultar el historial médico del paciente. Inténtalo nuevamente.",
          );
        }
      } finally {
        if (isMounted) {
          setIsLoading(false);
        }
      }
    }

    void loadHistory();

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

  return (
    <div
      className="patient-history-dialog"
      role="presentation"
      onMouseDown={(event) => {
        if (event.target === event.currentTarget) {
          handleClose();
        }
      }}
    >
      <section
        className="patient-history-dialog__panel"
        role="dialog"
        aria-modal="true"
        aria-labelledby={titleId}
        aria-describedby={descriptionId}
      >
        <header className="patient-history-dialog__header">
          <button
            type="button"
            className="patient-history-dialog__close"
            aria-label="Cerrar historial"
            onClick={handleClose}
          >
            <X size={20} />
          </button>

          <span
            className="patient-history-dialog__icon"
            aria-hidden="true"
          >
            <FileClock size={28} />
          </span>

          <h2 id={titleId}>
            Historial de observaciones médicas
          </h2>

          <p
            id={descriptionId}
            className="patient-history-dialog__description"
          >
            Consulta las observaciones registradas en atenciones anteriores del paciente.
          </p>

          <div className="patient-history-dialog__patient">
            <UserRound size={19} />

            <div>
              <span>Paciente</span>

              <strong>
                {appointment.patientFirstName}
                {" "}
                {appointment.patientLastName}
              </strong>
            </div>
          </div>

          {!isLoading
            && !errorMessage
            && history.length > 0 && (
              <div className="patient-history-dialog__history-heading">
                <div>
                  <CalendarDays size={18} />

                  <strong>
                    Atenciones anteriores
                  </strong>

                  <span className="patient-history-dialog__count">
                    {history.length}
                  </span>
                </div>

                <span>
                  Más recientes primero
                </span>
              </div>
            )}
        </header>

        <div className="patient-history-dialog__body">
          {isLoading && (
            <div className="patient-history-dialog__state">
              <span className="patient-history-dialog__spinner" />

              <h3>Consultando historial</h3>

              <p>
                Estamos cargando las atenciones anteriores del paciente.
              </p>
            </div>
          )}

          {!isLoading && errorMessage && (
            <div className="patient-history-dialog__state patient-history-dialog__state--error">
              <h3>No fue posible consultar el historial</h3>

              <p>{errorMessage}</p>
            </div>
          )}

          {!isLoading
            && !errorMessage
            && history.length === 0 && (
              <div className="patient-history-dialog__state">
                <FileClock size={34} />

                <h3>Sin observaciones anteriores</h3>

                <p>
                  Este paciente todavía no registra atenciones previas con observaciones médicas.
                </p>
              </div>
            )}

          {!isLoading
            && !errorMessage
            && history.length > 0 && (
              <div className="patient-history-dialog__timeline">
                {history.map((item) => {
                  const recordedAt =
                    formatRecordedAt(item.recordedAt);

                  return (
                    <article
                      key={item.appointmentId}
                      className="patient-history-dialog__item"
                    >
                      <span
                        className="patient-history-dialog__timeline-point"
                        aria-hidden="true"
                      />

                      <div className="patient-history-dialog__date">
                        <strong>
                          {formatLongDate(
                            item.appointmentDate,
                          )}
                        </strong>

                        <span>
                          <Clock3 size={16} />
                          {formatTime(item.startTime)}
                        </span>
                      </div>

                      <div className="patient-history-dialog__content">
                        <div className="patient-history-dialog__doctor">
                          <Stethoscope size={18} />

                          <div>
                            <span>
                              Médico tratante
                            </span>

                            <strong>
                              Dr. {item.doctorFirstName}
                              {" "}
                              {item.doctorLastName}
                            </strong>

                            <small>
                              {item.specialtyName}
                            </small>
                          </div>
                        </div>

                        <div className="patient-history-dialog__observation">
                          <span>
                            Observación médica
                          </span>

                          <p>
                            {item.medicalObservation}
                          </p>
                        </div>

                        {(recordedAt || item.recordedBy) && (
                          <footer className="patient-history-dialog__metadata">
                            {recordedAt && (
                              <span>
                                Registrada el {recordedAt}
                              </span>
                            )}

                            {item.recordedBy && (
                              <span>
                                Por {item.recordedBy}
                              </span>
                            )}
                          </footer>
                        )}
                      </div>
                    </article>
                  );
                })}
              </div>
            )}
        </div>

        <footer className="patient-history-dialog__footer">
          <div className="patient-history-dialog__footer-note">
            <Info size={18} />

            <span>
              Solo se muestran atenciones finalizadas con observación médica registrada.
            </span>
          </div>

          <AppButton
            type="button"
            fullWidth={false}
            onClick={handleClose}
          >
            Cerrar
          </AppButton>
        </footer>
      </section>
    </div>
  );
}

export default PatientHistoryDialog;
