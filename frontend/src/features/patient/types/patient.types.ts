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