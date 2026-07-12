export interface PatientFormValues {
  firstName: string;
  lastName: string;
  documentType: string;
  documentNumber: string;
  birthDate: string;
  phone: string;
  email: string;
  address: string;
}

export type PatientFormErrors = Partial<
  Record<keyof PatientFormValues, string>
>;

const EMAIL_PATTERN =
  /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

function isFutureDate(value: string): boolean {
  if (!value) {
    return false;
  }

  const selectedDate = new Date(`${value}T00:00:00`);
  const today = new Date();

  today.setHours(0, 0, 0, 0);

  return selectedDate.getTime() > today.getTime();
}

export function validatePatientForm(
  values: PatientFormValues,
): PatientFormErrors {
  const errors: PatientFormErrors = {};

  if (!values.firstName.trim()) {
    errors.firstName = "Ingresa los nombres del paciente.";
  }

  if (!values.lastName.trim()) {
    errors.lastName = "Ingresa los apellidos del paciente.";
  }

  if (!values.documentType) {
    errors.documentType = "Selecciona el tipo de documento.";
  }

  if (!values.documentNumber.trim()) {
    errors.documentNumber = "Ingresa el número de documento.";
  }

  if (!values.birthDate) {
    errors.birthDate = "Selecciona la fecha de nacimiento.";
  } else if (isFutureDate(values.birthDate)) {
    errors.birthDate =
      "La fecha de nacimiento no puede ser futura.";
  }

  if (!values.phone.trim()) {
    errors.phone = "Ingresa el teléfono del paciente.";
  }

  const normalizedEmail = values.email.trim();

  if (
    normalizedEmail &&
    !EMAIL_PATTERN.test(normalizedEmail)
  ) {
    errors.email =
      "Ingresa un correo electrónico válido.";
  }

  return errors;
}