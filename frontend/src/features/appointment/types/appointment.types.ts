export interface CreateAppointmentRequest {
  patientId: number;
  doctorId: number;
  agendaBlockId: number;
  reason: string | null;
  notes: string | null;
}

export interface CancelAppointmentRequest {
  reason: string | null;
}

export interface RegisterAppointmentNoShowRequest {
  comment: string | null;
}

export interface RescheduleAppointmentRequest {
  agendaBlockId: number;
}

export interface AppointmentResponse {
  id: number;
  clinicId: number;

  patientId: number;
  patientFirstName: string;
  patientLastName: string;

  doctorId: number;
  doctorFirstName: string;
  doctorLastName: string;

  agendaBlockId: number;
  appointmentDate: string;
  startTime: string;
  endTime: string;

  statusCode: string;
  statusName: string;

  reason: string | null;
  notes: string | null;

  cancellationReason: string | null;
  cancelledAt: string | null;

  recordStatus: string;
}

export interface AppointmentAgendaResponse {
  id: number;

  patientId: number;
  patientFirstName: string;
  patientLastName: string;

  doctorId: number;
  doctorFirstName: string;
  doctorLastName: string;

  specialtyId: number;
  specialtyName: string;

  agendaBlockId: number;
  appointmentDate: string;
  startTime: string;
  endTime: string;

  statusCode: string;
  statusName: string;

  reason: string | null;
}

export interface AppointmentAgendaFilters {
  date: string;
  doctorId?: number;
  status?: string;
}