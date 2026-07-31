import {
  ApiClientError,
  apiGet,
  apiPatch,
  apiPost,
} from "../../../shared/api/apiClient";

import type {
  AppointmentAgendaFilters,
  AppointmentAgendaResponse,
  AppointmentResponse,
  CancelAppointmentRequest,
  CreateAppointmentRequest,
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
  | "find"
  | "cancel"
  | "reschedule";

function getUserMessage(
  status: number,
  operation: AppointmentOperation,
): string {
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