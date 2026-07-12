export interface CreatePatientRequest {
  firstName: string;
  lastName: string;
  documentType: string;
  documentNumber: string;
  birthDate: string;
  phone: string;
  email: string | null;
  address: string | null;
}

export interface PatientResponse {
  id: number;
  clinicId: number;
  userId: number | null;
  firstName: string;
  lastName: string;
  documentType: string;
  documentNumber: string;
  birthDate: string;
  phone: string;
  email: string | null;
  address: string | null;
  recordStatus: string;
}