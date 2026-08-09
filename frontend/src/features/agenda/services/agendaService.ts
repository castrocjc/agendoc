import {
  ApiClientError,
  apiGet,
  apiPost,
} from "../../../shared/api/apiClient";

import type {
  AgendaBlockResponse,
  CreateAgendaBlocksRequest,
  CreateAgendaBlocksResponse,
} from "../types/agenda.types";

export class AgendaServiceError extends Error {
  readonly status: number;
  readonly code?: string;

  constructor(
    message: string,
    status: number,
    code?: string,
  ) {
    super(message);
    this.name = "AgendaServiceError";
    this.status = status;
    this.code = code;
  }
}

function getUserMessage(status: number): string {
  switch (status) {
    case 0:
      return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

    case 400:
      return "Revisa la fecha y los horarios ingresados.";

    case 404:
      return "No se encontró el médico o la agenda solicitada.";

    case 409:
      return "Uno o más bloques se superponen con horarios ya registrados.";

    default:
      return "No fue posible completar la operación. Inténtalo nuevamente.";
  }
}

function mapServiceError(error: unknown): AgendaServiceError {
  if (error instanceof ApiClientError) {
    const message =
      error.apiMessage?.trim() || getUserMessage(error.status);

    return new AgendaServiceError(
      message,
      error.status,
      error.code,
    );
  }

  return new AgendaServiceError(
    "No fue posible completar la operación. Inténtalo nuevamente.",
    500,
  );
}

export async function createAgendaBlocks(
  doctorId: number,
  request: CreateAgendaBlocksRequest,
): Promise<CreateAgendaBlocksResponse> {
  try {
    return await apiPost<
      CreateAgendaBlocksResponse,
      CreateAgendaBlocksRequest
    >(
      `/api/v1/doctors/${doctorId}/agenda-blocks`,
      request,
    );
  } catch (error) {
    throw mapServiceError(error);
  }
}

export async function findAgendaBlocks(
  doctorId: number,
  appointmentDate: string,
): Promise<AgendaBlockResponse[]> {
  const params = new URLSearchParams({
    appointmentDate,
  });

  try {
    return await apiGet<AgendaBlockResponse[]>(
      `/api/v1/doctors/${doctorId}/agenda-blocks?${params.toString()}`,
    );
  } catch (error) {
    throw mapServiceError(error);
  }
}