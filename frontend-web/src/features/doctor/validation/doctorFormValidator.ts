export interface DoctorFormValues {
  firstName: string;
  lastName: string;
  documentType: string;
  documentNumber: string;
  medicalLicenseNumber: string;
  specialtyId: string;
  phone: string;
  email: string;
}

export type DoctorFormErrors = Partial<
  Record<keyof DoctorFormValues, string>
>;

const EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

export function validateDoctorForm(
  values: DoctorFormValues,
): DoctorFormErrors {
  const errors: DoctorFormErrors = {};

  const firstName = values.firstName.trim();
  const lastName = values.lastName.trim();
  const documentNumber = values.documentNumber.trim();
  const medicalLicenseNumber =
    values.medicalLicenseNumber.trim();
  const phone = values.phone.trim();
  const email = values.email.trim();

  if (!firstName) {
    errors.firstName = "Los nombres son obligatorios.";
  } else if (firstName.length > 100) {
    errors.firstName =
      "Los nombres no pueden superar 100 caracteres.";
  }

  if (!lastName) {
    errors.lastName = "Los apellidos son obligatorios.";
  } else if (lastName.length > 100) {
    errors.lastName =
      "Los apellidos no pueden superar 100 caracteres.";
  }

  if (!values.documentType) {
    errors.documentType =
      "Selecciona un tipo de documento.";
  } else if (values.documentType.length > 30) {
    errors.documentType =
      "El tipo de documento no puede superar 30 caracteres.";
  }

  if (!documentNumber) {
    errors.documentNumber =
      "El número de documento es obligatorio.";
  } else if (documentNumber.length > 50) {
    errors.documentNumber =
      "El número de documento no puede superar 50 caracteres.";
  }

  if (!values.specialtyId) {
    errors.specialtyId =
      "Selecciona una especialidad médica.";
  }

  if (!medicalLicenseNumber) {
    errors.medicalLicenseNumber =
      "El número de colegiatura es obligatorio.";
  } else if (medicalLicenseNumber.length > 50) {
    errors.medicalLicenseNumber =
      "El número de colegiatura no puede superar 50 caracteres.";
  }

  if (!email) {
    errors.email = "El correo es obligatorio.";
  } else if (!EMAIL_PATTERN.test(email)) {
    errors.email = "El correo no tiene un formato válido.";
  } else if (email.length > 150) {
    errors.email =
      "El correo no puede superar 150 caracteres.";
  }

  if (phone.length > 30) {
    errors.phone =
      "El teléfono no puede superar 30 caracteres.";
  }

  return errors;
}