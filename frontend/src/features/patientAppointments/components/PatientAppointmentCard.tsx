import {
  Ban,
  Clock3,
  LoaderCircle,
  Stethoscope,
} from "lucide-react";

import AppButton from "../../../components/AppButton";
import AppCard from "../../../components/AppCard";
import type {
  AppointmentStatusCode,
  PatientAppointmentResponse,
} from "../../appointment/types/appointment.types";

import "./PatientAppointmentCard.css";

interface PatientAppointmentCardProps {
  appointment: PatientAppointmentResponse;
  cancellable: boolean;
  cancelling: boolean;
  onCancelRequest: (
    appointment: PatientAppointmentResponse,
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

function getStatusClassName(
  statusCode: AppointmentStatusCode,
): string {
  return statusCode
    .toLowerCase()
    .replace("_", "-");
}

function PatientAppointmentCard({
  appointment,
  cancellable,
  cancelling,
  onCancelRequest,
}: PatientAppointmentCardProps) {
  return (
    <AppCard
      className="patient-appointment-card"
      elevation="low"
    >
      <div className="patient-appointment-card__header">
        <div>
          <p className="patient-appointment-card__date">
            {formatLongDate(
              appointment.appointmentDate,
            )}
          </p>

          <p className="patient-appointment-card__time">
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
          className={`patient-appointment-card__status patient-appointment-card__status--${getStatusClassName(
            appointment.statusCode,
          )}`}
        >
          {appointment.statusName}
        </span>
      </div>

      <div className="patient-appointment-card__doctor">
        <span
          className="patient-appointment-card__doctor-icon"
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
        <div className="patient-appointment-card__detail">
          <span>Motivo de consulta</span>

          <p>{appointment.reason}</p>
        </div>
      )}

      {appointment.statusCode === "CANCELADA"
        && appointment.cancellationReason && (
          <div className="patient-appointment-card__detail patient-appointment-card__detail--cancelled">
            <span>Motivo de cancelación</span>

            <p>
              {appointment.cancellationReason}
            </p>
          </div>
        )}

      {cancellable && (
        <div className="patient-appointment-card__actions">
          <AppButton
            type="button"
            variant="secondary"
            fullWidth={false}
            leftIcon={
              cancelling
                ? <LoaderCircle size={17} />
                : <Ban size={17} />
            }
            disabled={cancelling}
            onClick={() => {
              onCancelRequest(appointment);
            }}
          >
            {cancelling
              ? "Cancelando..."
              : "Cancelar cita"}
          </AppButton>
        </div>
      )}
    </AppCard>
  );
}

export default PatientAppointmentCard;