export interface CreateAppointmentRequest {
  patientId: number;
  doctorId: number;
  agendaBlockId: number;
  reason: string | null;
  notes: string | null;
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
  recordStatus: string;
}