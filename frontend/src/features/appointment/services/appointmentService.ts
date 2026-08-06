import {
  ApiClientError,
  apiGet,
  apiPatch,
  apiPost,
} from "../../../shared/api/apiClient";

import type {
  AppointmentAgendaFilters,
  AppointmentAgendaResponse,
  DoctorAppointmentAgendaFilters,
  AppointmentResponse,

  PatientAppointmentResponse,
  CancelAppointmentRequest,
  CreateAppointmentRequest,
  CreatePatientAppointmentRequest,
  RegisterAppointmentNoShowRequest,
  RegisterMedicalObservationRequest,
  MedicalObservationResponse,
  RescheduleAppointmentRequest,
} from "../types/appointment.types";

export class AppointmentServiceError extends Error {
  readonly status: number;
  readonly code?: string;

  constructor(
    message: string,
    status: number,
    code?: string,
  ) {
    super(message);
    this.name = "AppointmentServiceError";
    this.status = status;
    this.code = code;
  }
}

type AppointmentOperation =
  | "create"
  | "createPatient"
  | "find"
  | "findDoctor"
  | "findPatient"
  | "cancel"
  | "cancelPatient"
  | "confirmArrival"
  | "registerNoShow"
  | "reschedule"
  | "findMedicalObservation"
  | "registerMedicalObservation"
  | "markAsAttended";

function getUserMessage(
  status: number,
  operation: AppointmentOperation,
): string {
  if (operation === "createPatient") {
    switch (status) {
      case 0:
        return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

      case 400:
        return "Revisa el médico, el horario y la información de la cita.";

      case 403:
        return "Tu cuenta no tiene autorización para reservar citas.";

      case 404:
        return "El médico o el horario seleccionado ya no se encuentra disponible.";

      case 409:
        return "El horario seleccionado ya fue reservado o coincide con otra cita programada.";

      default:
        return "No fue posible reservar tu cita. Inténtalo nuevamente.";
    }
  }

  if (operation === "findDoctor") {
    switch (status) {
      case 0:
        return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

      case 400:
        return "La fecha seleccionada no es válida.";

      case 403:
        return "Tu cuenta no tiene autorización para consultar la agenda médica.";

      case 404:
        return "No fue posible encontrar el perfil médico asociado con tu cuenta.";

      default:
        return "No fue posible consultar tu agenda médica. Inténtalo nuevamente.";
    }
  }

  if (operation === "findPatient") {
    switch (status) {
      case 0:
        return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

      case 403:
        return "Tu cuenta no tiene autorización para consultar estas citas.";

      case 404:
        return "No fue posible encontrar el perfil de paciente asociado con tu cuenta.";

      default:
        return "No fue posible consultar tus citas. Inténtalo nuevamente.";
    }
  }

  if (operation === "cancelPatient") {
    switch (status) {
      case 0:
        return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

      case 400:
        return "El motivo de cancelación no puede exceder 500 caracteres.";

      case 403:
        return "No tienes autorización para cancelar esta cita.";

      case 404:
        return "La cita seleccionada ya no se encuentra disponible.";

      case 409:
        return "Esta cita ya no puede ser cancelada.";

      default:
        return "No fue posible cancelar tu cita. Inténtalo nuevamente.";
    }
  }

  if (operation === "find") {
    switch (status) {
      case 0:
        return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

      case 400:
        return "La fecha seleccionada no es válida.";

      case 403:
        return "No tienes autorización para consultar la agenda del consultorio.";

      case 404:
        return "No se encontró la información solicitada.";

      default:
        return "No fue posible consultar la agenda del consultorio. Inténtalo nuevamente.";
    }
  }

  if (operation === "cancel") {
    switch (status) {
      case 0:
        return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

      case 400:
        return "Revisa la información ingresada para cancelar la cita.";

      case 403:
        return "No tienes autorización para cancelar citas médicas.";

      case 404:
        return "La cita seleccionada no se encuentra disponible.";

      case 409:
        return "La cita no puede cancelarse en su estado actual.";

      default:
        return "No fue posible cancelar la cita médica. Inténtalo nuevamente.";
    }
  }

  if (operation === "confirmArrival") {
    switch (status) {
      case 0:
        return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

      case 403:
        return "No tienes autorización para confirmar la llegada del paciente.";

      case 404:
        return "La cita seleccionada no se encuentra disponible.";

      case 409:
        return "La llegada del paciente no puede confirmarse en el estado actual de la cita.";

      default:
        return "No fue posible confirmar la llegada del paciente. Inténtalo nuevamente.";
    }
  }

  if (operation === "registerNoShow") {
    switch (status) {
      case 0:
        return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

      case 400:
        return "La cita solo puede marcarse como no asistida cuando haya comenzado su horario.";

      case 403:
        return "No tienes autorización para registrar la inasistencia del paciente.";

      case 404:
        return "La cita seleccionada no se encuentra disponible.";

      case 409:
        return "La inasistencia no puede registrarse en el estado actual de la cita.";

      default:
        return "No fue posible registrar la inasistencia del paciente. Inténtalo nuevamente.";
    }
  }

  if (operation === "reschedule") {
    switch (status) {
      case 0:
        return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

      case 400:
        return "Revisa la fecha y el horario seleccionados para reprogramar la cita.";

      case 403:
        return "No tienes autorización para reprogramar citas médicas.";

      case 404:
        return "La cita o el bloque de agenda seleccionado no se encuentra disponible.";

      case 409:
        return "La cita no puede reprogramarse al horario seleccionado.";

      default:
        return "No fue posible reprogramar la cita médica. Inténtalo nuevamente.";
    }
  }

  if (operation === "findMedicalObservation") {
    switch (status) {
      case 0:
        return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

      case 403:
        return "Tu cuenta no tiene autorización para consultar esta observación médica.";

      case 404:
        return "La cita seleccionada ya no se encuentra disponible.";

      default:
        return "No fue posible consultar la observación médica. Inténtalo nuevamente.";
    }
  }

  if (operation === "registerMedicalObservation") {
    switch (status) {
      case 0:
        return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

      case 400:
        return "Revisa el contenido de la observación médica.";

      case 403:
        return "Tu cuenta no tiene autorización para registrar esta observación médica.";

      case 404:
        return "La cita seleccionada ya no se encuentra disponible.";

      case 409:
        return "La observación médica no puede registrarse en el estado actual de la cita.";

      default:
        return "No fue posible guardar la observación médica. Inténtalo nuevamente.";
    }
  }

  if (operation === "markAsAttended") {
    switch (status) {
      case 0:
        return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

      case 400:
        return "Debe registrar una observación médica antes de marcar la cita como atendida.";

      case 403:
        return "Tu cuenta no tiene autorización para cerrar esta atención médica.";

      case 404:
        return "La cita seleccionada ya no se encuentra disponible.";

      case 409:
        return "La cita no puede marcarse como atendida en su estado actual.";

      default:
        return "No fue posible marcar la cita como atendida. Inténtalo nuevamente.";
    }
  }

  switch (status) {
    case 0:
      return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

    case 400:
      return "Revisa la información ingresada para agendar la cita.";

    case 403:
      return "No tienes autorización para registrar citas médicas.";

    case 404:
      return "No se encontró el paciente, médico o bloque de agenda seleccionado.";

    case 409:
      return "El horario seleccionado ya no se encuentra disponible.";

    default:
      return "No fue posible crear la cita médica. Inténtalo nuevamente.";
  }
}

function mapServiceError(
  error: unknown,
  operation: AppointmentOperation,
): AppointmentServiceError {
  if (error instanceof ApiClientError) {
    const message =
      error.apiMessage?.trim()
      || getUserMessage(error.status, operation);

    return new AppointmentServiceError(
      message,
      error.status,
      error.code,
    );
  }

  return new AppointmentServiceError(
    getUserMessage(500, operation),
    500,
  );
}

export async function createAppointment(
  request: CreateAppointmentRequest,
): Promise<AppointmentResponse> {
  try {
    return await apiPost<
      AppointmentResponse,
      CreateAppointmentRequest
    >(
      "/api/v1/appointments",
      request,
    );
  } catch (error) {
    throw mapServiceError(error, "create");
  }
}

export async function createPatientAppointment(
  request: CreatePatientAppointmentRequest,
): Promise<AppointmentResponse> {
  try {
    return await apiPost<
      AppointmentResponse,
      CreatePatientAppointmentRequest
    >(
      "/api/v1/appointments/patient",
      request,
    );
  } catch (error) {
    throw mapServiceError(error, "createPatient");
  }
}

export async function cancelAppointment(
  appointmentId: number,
  request: CancelAppointmentRequest,
): Promise<AppointmentResponse> {
  try {
    return await apiPatch<
      AppointmentResponse,
      CancelAppointmentRequest
    >(
      `/api/v1/appointments/${appointmentId}/cancel`,
      request,
    );
  } catch (error) {
    throw mapServiceError(error, "cancel");
  }
}

export async function cancelPatientAppointment(
  appointmentId: number,
  reason: string | null,
): Promise<AppointmentResponse> {
  const request: CancelAppointmentRequest = {
    reason,
  };

  try {
    return await apiPatch<
      AppointmentResponse,
      CancelAppointmentRequest
    >(
      `/api/v1/appointments/${appointmentId}/cancel`,
      request,
    );
  } catch (error) {
    throw mapServiceError(
      error,
      "cancelPatient",
    );
  }
}

export async function confirmAppointmentArrival(
  appointmentId: number,
): Promise<AppointmentResponse> {
  try {
    return await apiPatch<
      AppointmentResponse,
      undefined
    >(
      `/api/v1/appointments/${appointmentId}/confirm-arrival`,
      undefined,
    );
  } catch (error) {
    throw mapServiceError(error, "confirmArrival");
  }
}

export async function registerAppointmentNoShow(
  appointmentId: number,
  request: RegisterAppointmentNoShowRequest,
): Promise<AppointmentResponse> {
  try {
    return await apiPatch<
      AppointmentResponse,
      RegisterAppointmentNoShowRequest
    >(
      `/api/v1/appointments/${appointmentId}/no-show`,
      request,
    );
  } catch (error) {
    throw mapServiceError(error, "registerNoShow");
  }
}

export async function rescheduleAppointment(
  appointmentId: number,
  request: RescheduleAppointmentRequest,
): Promise<AppointmentResponse> {
  try {
    return await apiPatch<
      AppointmentResponse,
      RescheduleAppointmentRequest
    >(
      `/api/v1/appointments/${appointmentId}/reschedule`,
      request,
    );
  } catch (error) {
    throw mapServiceError(error, "reschedule");
  }
}

export async function findAppointments(
  filters: AppointmentAgendaFilters,
): Promise<AppointmentAgendaResponse[]> {
  const searchParameters = new URLSearchParams();

  searchParameters.set("date", filters.date);

  if (filters.doctorId !== undefined) {
    searchParameters.set(
      "doctorId",
      filters.doctorId.toString(),
    );
  }

  const normalizedStatus = filters.status?.trim();

  if (normalizedStatus) {
    searchParameters.set(
      "status",
      normalizedStatus,
    );
  }

  try {
    return await apiGet<AppointmentAgendaResponse[]>(
      `/api/v1/appointments?${searchParameters.toString()}`,
    );
  } catch (error) {
    throw mapServiceError(error, "find");
  }
}
export async function findDoctorAppointments(
  filters: DoctorAppointmentAgendaFilters,
): Promise<AppointmentAgendaResponse[]> {
  const searchParameters = new URLSearchParams();

  searchParameters.set("date", filters.date);

  const normalizedStatus = filters.status?.trim();

  if (normalizedStatus) {
    searchParameters.set(
      "status",
      normalizedStatus,
    );
  }

  try {
    return await apiGet<AppointmentAgendaResponse[]>(
      `/api/v1/appointments/doctor?${searchParameters.toString()}`,
    );
  } catch (error) {
    throw mapServiceError(error, "findDoctor");
  }
}

export async function findMedicalObservation(
  appointmentId: number,
): Promise<MedicalObservationResponse> {
  try {
    return await apiGet<MedicalObservationResponse>(
      `/api/v1/appointments/${appointmentId}/medical-observation`,
    );
  } catch (error) {
    throw mapServiceError(
      error,
      "findMedicalObservation",
    );
  }
}

export async function registerMedicalObservation(
  appointmentId: number,
  request: RegisterMedicalObservationRequest,
): Promise<MedicalObservationResponse> {
  try {
    return await apiPatch<
      MedicalObservationResponse,
      RegisterMedicalObservationRequest
    >(
      `/api/v1/appointments/${appointmentId}/medical-observation`,
      request,
    );
  } catch (error) {
    throw mapServiceError(
      error,
      "registerMedicalObservation",
    );
  }
}

export async function markAppointmentAsAttended(
  appointmentId: number,
): Promise<AppointmentResponse> {
  try {
    return await apiPatch<
      AppointmentResponse,
      undefined
    >(
      `/api/v1/appointments/${appointmentId}/attend`,
      undefined,
    );
  } catch (error) {
    throw mapServiceError(
      error,
      "markAsAttended",
    );
  }
}

export async function findPatientAppointments(): Promise<
  PatientAppointmentResponse[]
> {
  try {
    return await apiGet<PatientAppointmentResponse[]>(
      "/api/v1/appointments/patient",
    );
  } catch (error) {
    throw mapServiceError(error, "findPatient");
  }
}
