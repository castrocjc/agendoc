import {
  useCallback,
  useEffect,
  useId,
  useState,
} from "react";

import {
  Ban,
  CalendarDays,
  Clock3,
  Stethoscope,
  X,
} from "lucide-react";

import AppButton from "../../../components/AppButton";
import type {
  PatientAppointmentResponse,
} from "../../appointment/types/appointment.types";

import "./CancelPatientAppointmentDialog.css";

const MAX_REASON_LENGTH = 500;

interface CancelPatientAppointmentDialogProps {
  appointment: PatientAppointmentResponse | null;
  open: boolean;
  submitting: boolean;
  errorMessage: string;
  onClose: () => void;
  onConfirm: (
    reason: string | null,
  ) => void;
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

function CancelPatientAppointmentDialog({
  appointment,
  open,
  submitting,
  errorMessage,
  onClose,
  onConfirm,
}: CancelPatientAppointmentDialogProps) {
  const titleId = useId();
  const descriptionId = useId();

  const [reason, setReason] = useState("");

  const handleClose = useCallback((): void => {
    if (submitting) {
      return;
    }

    setReason("");
    onClose();
  }, [
    onClose,
    submitting,
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

  const normalizedReason = reason.trim();

  return (
    <div
      className="cancel-patient-appointment-dialog"
      role="presentation"
      onMouseDown={(event) => {
        if (event.target === event.currentTarget) {
          handleClose();
        }
      }}
    >
      <section
        className="cancel-patient-appointment-dialog__panel"
        role="dialog"
        aria-modal="true"
        aria-labelledby={titleId}
        aria-describedby={descriptionId}
      >
        <button
          type="button"
          className="cancel-patient-appointment-dialog__close"
          aria-label="Cerrar diálogo"
          disabled={submitting}
          onClick={handleClose}
        >
          <X size={20} />
        </button>

        <span
          className="cancel-patient-appointment-dialog__icon"
          aria-hidden="true"
        >
          <Ban size={28} />
        </span>

        <h2 id={titleId}>
          Cancelar cita
        </h2>

        <p
          id={descriptionId}
          className="cancel-patient-appointment-dialog__description"
        >
          Confirma que deseas cancelar esta cita. El horario volverá a quedar disponible.
        </p>

        <div className="cancel-patient-appointment-dialog__summary">
          <div>
            <Stethoscope size={18} />

            <span>
              {getDoctorName(appointment)}
              {" · "}
              {appointment.specialtyName}
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

        <label className="cancel-patient-appointment-dialog__field">
          <span>
            Motivo de cancelación
            <small>Opcional</small>
          </span>

          <textarea
            value={reason}
            maxLength={MAX_REASON_LENGTH}
            rows={4}
            disabled={submitting}
            placeholder="Cuéntanos brevemente por qué necesitas cancelar la cita."
            onChange={(event) => {
              setReason(event.target.value);
            }}
          />
        </label>

        <div className="cancel-patient-appointment-dialog__counter">
          {reason.length}
          {" / "}
          {MAX_REASON_LENGTH}
        </div>

        {errorMessage && (
          <p
            className="cancel-patient-appointment-dialog__error"
            role="alert"
          >
            {errorMessage}
          </p>
        )}

        <div className="cancel-patient-appointment-dialog__actions">
          <AppButton
            type="button"
            variant="ghost"
            fullWidth={false}
            disabled={submitting}
            onClick={handleClose}
          >
            Mantener cita
          </AppButton>

          <AppButton
            type="button"
            variant="secondary"
            fullWidth={false}
            leftIcon={<Ban size={17} />}
            disabled={submitting}
            onClick={() => {
              onConfirm(
                normalizedReason || null,
              );
            }}
          >
            {submitting
              ? "Cancelando..."
              : "Confirmar cancelación"}
          </AppButton>
        </div>
      </section>
    </div>
  );
}

export default CancelPatientAppointmentDialog;