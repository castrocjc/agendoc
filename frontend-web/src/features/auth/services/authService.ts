import type { LoginRequest, LoginResponse } from "../types/auth.types";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081";

interface ApiErrorResponse {
  code?: string;
  message?: string;
  timestamp?: string;
  detail?: string;
}

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

async function readErrorResponse(
  response: Response,
): Promise<ApiErrorResponse | null> {
  try {
    return (await response.json()) as ApiErrorResponse;
  } catch {
    return null;
  }
}

function getUserMessage(status: number): string {
  switch (status) {
    case 400:
      return "Revisa los datos ingresados e inténtalo nuevamente.";

    case 401:
      return "El usuario o la contraseña son incorrectos.";

    default:
      return "No pudimos iniciar sesión. Inténtalo nuevamente en unos momentos.";
  }
}

export async function login(request: LoginRequest): Promise<LoginResponse> {
  let response: Response;

  try {
    response = await fetch(`${API_BASE_URL}/api/v1/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(request),
    });
  } catch {
    throw new AuthenticationError(
      "No fue posible conectarse con AgenDoc. Verifica tu conexión e inténtalo nuevamente.",
      0,
    );
  }

  if (!response.ok) {
    const errorResponse = await readErrorResponse(response);

    throw new AuthenticationError(
      getUserMessage(response.status),
      response.status,
      errorResponse?.code,
    );
  }

  try {
    return (await response.json()) as LoginResponse;
  } catch {
    throw new AuthenticationError(
      "La respuesta recibida no pudo procesarse correctamente.",
      500,
    );
  }
}