export interface AppointmentFormValues {
  patientId: string;
  doctorId: string;
  agendaBlockId: string;
  reason: string;
  notes: string;
}

export interface AppointmentFormErrors {
  patientId: string;
  doctorId: string;
  agendaBlockId: string;
  reason: string;
  notes: string;
}

export interface AppointmentValidationResult {
  isValid: boolean;
  errors: AppointmentFormErrors;
}

const MAX_REASON_LENGTH = 500;
const MAX_NOTES_LENGTH = 1000;

export function validateAppointment(
  values: AppointmentFormValues,
): AppointmentValidationResult {
  const errors: AppointmentFormErrors = {
    patientId: "",
    doctorId: "",
    agendaBlockId: "",
    reason: "",
    notes: "",
  };

  if (!values.patientId) {
    errors.patientId =
      "Selecciona un paciente para agendar la cita.";
  }

  if (!values.doctorId) {
    errors.doctorId =
      "Selecciona un médico para agendar la cita.";
  }

  if (!values.agendaBlockId) {
    errors.agendaBlockId =
      "Selecciona un horario disponible.";
  }

  if (values.reason.trim().length > MAX_REASON_LENGTH) {
    errors.reason =
      `El motivo de la cita no puede superar los ${MAX_REASON_LENGTH} caracteres.`;
  }

  if (values.notes.trim().length > MAX_NOTES_LENGTH) {
    errors.notes =
      `Las observaciones no pueden superar los ${MAX_NOTES_LENGTH} caracteres.`;
  }

  return {
    isValid: Object.values(errors).every(
      (error) => error.length === 0,
    ),
    errors,
  };
}