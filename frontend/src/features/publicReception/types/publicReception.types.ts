export interface PublicClinic {
  slug: string;
  publicName: string;
  publicDescription: string | null;
  logoUrl: string | null;
  phone: string | null;
  whatsapp: string | null;
  email: string | null;
  address: string | null;
  mapUrl: string | null;
}

export interface PublicSpecialty {
  id: number;
  name: string;
}

export interface PublicDoctorSummary {
  id: number;
  firstName: string;
  lastName: string;
  specialtyId: number;
  specialtyName: string;
}

export interface PublicAgendaAvailability {
  agendaBlockId: number;
  appointmentDate: string;
  startTime: string;
  endTime: string;
}

export interface FirstAppointmentRequest {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  password: string;
  passwordConfirmation: string;
  doctorId: number;
  agendaBlockId: number;
  reason?: string;
}

export interface FirstAppointmentResponse {
  appointmentId: number;
  firstName: string;
  lastName: string;
  email: string;
  doctorId: number;
  doctorFirstName: string;
  doctorLastName: string;
  specialtyId: number;
  specialtyName: string;
  appointmentDate: string;
  startTime: string;
  endTime: string;
  statusCode: string;
  statusName: string;
}
