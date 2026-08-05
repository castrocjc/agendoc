import type {
  PatientAppointmentFormData,
  PatientAppointmentFormErrors,
} from "../types/patientAppointment.types";

export function validatePatientAppointmentForm(
  form: PatientAppointmentFormData,
): PatientAppointmentFormErrors {
  const errors: PatientAppointmentFormErrors = {};

  if (form.reason.trim().length > 500) {
    errors.reason =
      "El motivo de la cita no puede superar los 500 caracteres.";
  }

  if (form.notes.trim().length > 1000) {
    errors.notes =
      "Las observaciones no pueden superar los 1000 caracteres.";
  }

  return errors;
}
