export interface MedicalSpecialty {
  id: number;
  code: string;
  name: string;
  description: string;
}

export interface CreateDoctorRequest {
  firstName: string;
  lastName: string;
  documentType: string;
  documentNumber: string;
  medicalLicenseNumber: string;
  specialtyId: number;
  phone: string;
  email: string;
}

export interface DoctorResponse {
  id: number;
  clinicId: number;
  specialtyId: number;
  specialtyName: string;
  firstName: string;
  lastName: string;
  documentType: string;
  documentNumber: string;
  medicalLicenseNumber: string;
  phone: string;
  email: string;
  recordStatus: string;
}