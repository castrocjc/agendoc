import {
  ApiClientError,
  apiPost,
} from "../../../shared/api/apiClient";

import type {
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

function getUserMessage(status: number): string {
  switch (status) {
    case 0:
      return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

    case 400:
      return "Revisa la información ingresada para agendar la cita.";

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
): AppointmentServiceError {
  if (error instanceof ApiClientError) {
    const message =
      error.apiMessage?.trim() || getUserMessage(error.status);

    return new AppointmentServiceError(
      message,
      error.status,
      error.code,
    );
  }

  return new AppointmentServiceError(
    "No fue posible crear la cita médica. Inténtalo nuevamente.",
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
    throw mapServiceError(error);
  }
}