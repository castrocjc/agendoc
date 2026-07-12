import {
  ApiClientError,
  apiPost,
} from "../../../shared/api/apiClient";
import type {
  CreatePatientRequest,
  PatientResponse,
} from "../types/patient.types";

export class PatientServiceError extends Error {
  readonly status: number;
  readonly code?: string;

  constructor(message: string, status: number, code?: string) {
    super(message);
    this.name = "PatientServiceError";
    this.status = status;
    this.code = code;
  }
}

function getUserMessage(status: number): string {
  switch (status) {
    case 0:
      return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

    case 400:
      return "Revisa la información ingresada.";

    case 404:
      return "No se encontró la información solicitada.";

    case 409:
      return "Ya existe un paciente registrado con esos datos.";

    default:
      return "No fue posible completar la operación. Inténtalo nuevamente.";
  }
}

function mapServiceError(error: unknown): PatientServiceError {
  if (error instanceof ApiClientError) {
    const message =
      error.apiMessage?.trim() || getUserMessage(error.status);

    return new PatientServiceError(
      message,
      error.status,
      error.code,
    );
  }

  return new PatientServiceError(
    "No fue posible completar la operación. Inténtalo nuevamente.",
    500,
  );
}

export async function createPatient(
  request: CreatePatientRequest,
): Promise<PatientResponse> {
  try {
    return await apiPost<
      PatientResponse,
      CreatePatientRequest
    >(
      "/api/v1/patients",
      request,
    );
  } catch (error) {
    throw mapServiceError(error);
  }
}