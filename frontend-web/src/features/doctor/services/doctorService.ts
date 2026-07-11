import {
  ApiClientError,
  apiGet,
  apiPost,
} from "../../../shared/api/apiClient";
import type {
  CreateDoctorRequest,
  DoctorResponse,
  MedicalSpecialty,
} from "../types/doctor.types";

export class DoctorServiceError extends Error {
  readonly status: number;
  readonly code?: string;

  constructor(message: string, status: number, code?: string) {
    super(message);
    this.name = "DoctorServiceError";
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
      return "Ya existe un médico registrado con esos datos.";

    default:
      return "No fue posible completar la operación. Inténtalo nuevamente.";
  }
}

function mapServiceError(error: unknown): DoctorServiceError {
  if (error instanceof ApiClientError) {
    const message =
      error.apiMessage?.trim() || getUserMessage(error.status);

    return new DoctorServiceError(
      message,
      error.status,
      error.code,
    );
  }

  return new DoctorServiceError(
    "No fue posible completar la operación. Inténtalo nuevamente.",
    500,
  );
}

export async function getMedicalSpecialties(): Promise<
  MedicalSpecialty[]
> {
  try {
    return await apiGet<MedicalSpecialty[]>(
      "/api/v1/medical-specialties",
    );
  } catch (error) {
    throw mapServiceError(error);
  }
}

export async function createDoctor(
  request: CreateDoctorRequest,
): Promise<DoctorResponse> {
  try {
    return await apiPost<DoctorResponse, CreateDoctorRequest>(
      "/api/v1/doctors",
      request,
    );
  } catch (error) {
    throw mapServiceError(error);
  }
}