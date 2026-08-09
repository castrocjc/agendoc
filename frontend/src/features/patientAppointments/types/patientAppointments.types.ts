export type PatientAppointmentsFilter =
  | "upcoming"
  | "previous"
  | "cancelled"
  | "all";

export type PatientAppointmentsStatus =
  | "loading"
  | "success"
  | "error";
