import {
  ApiClientError,
  apiPost,
} from "../../../shared/api/apiClient";
import type {
  LoginRequest,
  LoginResponse,
} from "../types/auth.types";

export class AuthenticationError extends Error {
  readonly status: number;
  readonly code?: string;

  constructor(message: string, status: number, code?: string) {
    super(message);
    this.name = "AuthenticationError";
    this.status = status;
    this.code = code;
  }
}

function getUserMessage(status: number): string {
  switch (status) {
    case 0:
      return "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.";

    case 400:
      return "Revisa los datos ingresados e inténtalo nuevamente.";

    case 401:
      return "El usuario o la contraseña son incorrectos.";

    default:
      return "No pudimos iniciar sesión. Inténtalo nuevamente en unos momentos.";
  }
}

export async function login(
  request: LoginRequest,
): Promise<LoginResponse> {
  try {
    return await apiPost<LoginResponse, LoginRequest>(
      "/api/v1/auth/login",
      request,
    );
  } catch (error) {
    if (error instanceof ApiClientError) {
      throw new AuthenticationError(
        getUserMessage(error.status),
        error.status,
        error.code,
      );
    }

    throw new AuthenticationError(
      "No pudimos iniciar sesión. Inténtalo nuevamente en unos momentos.",
      500,
    );
  }
}