export type PatientAppointmentStep =
  | "specialty"
  | "doctor"
  | "availability"
  | "confirmation";

export type ResourceStatus =
  | "idle"
  | "loading"
  | "success"
  | "error";

export type BookingStatus =
  | "idle"
  | "submitting"
  | "success"
  | "error";

export interface SpecialtyOption {
  id: number;
  name: string;
}

export interface PatientAppointmentFormData {
  reason: string;
  notes: string;
}

export type PatientAppointmentFormErrors = Partial<
  Record<keyof PatientAppointmentFormData, string>
>;
