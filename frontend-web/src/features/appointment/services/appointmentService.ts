import {
  ApiClientError,
  apiGet,
  apiPost,
} from "../../../shared/api/apiClient";

import type {
  AppointmentAgendaFilters,
  AppointmentAgendaResponse,
  AppointmentResponse,
  CreateAppointmentRequest,
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
  | "find";

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