export interface AgendaBlockFormValues {
  doctorId: string;
  appointmentDate: string;
  startTime: string;
  endTime: string;
}

export interface AgendaBlockFormErrors {
  doctorId: string;
  appointmentDate: string;
  startTime: string;
  endTime: string;
}

export interface AgendaBlockValidationResult {
  isValid: boolean;
  errors: AgendaBlockFormErrors;
}

export function getToday(): string {
  const today = new Date();

  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, "0");
  const day = String(today.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
}

export function formatAgendaDate(value: string): string {
  const [year, month, day] = value.split("-");

  return `${day}/${month}/${year}`;
}

export function validateAgendaBlock(
  values: AgendaBlockFormValues,
): AgendaBlockValidationResult {
  const errors: AgendaBlockFormErrors = {
    doctorId: "",
    appointmentDate: "",
    startTime: "",
    endTime: "",
  };

  if (!values.doctorId) {
    errors.doctorId =
      "Selecciona un médico para crear bloques de agenda.";
  }

  if (!values.appointmentDate) {
    errors.appointmentDate = "La fecha es obligatoria.";
  } else if (values.appointmentDate < getToday()) {
    errors.appointmentDate =
      "La fecha no puede estar en el pasado.";
  }

  if (!values.startTime) {
    errors.startTime = "La hora de inicio es obligatoria.";
  }

  if (!values.endTime) {
    errors.endTime = "La hora de fin es obligatoria.";
  }

  if (
    values.startTime
    && values.endTime
    && values.startTime >= values.endTime
  ) {
    errors.endTime =
      "La hora de fin debe ser posterior a la hora de inicio.";
  }

  return {
    isValid: Object.values(errors).every(
      (error) => error.length === 0,
    ),
    errors,
  };
}