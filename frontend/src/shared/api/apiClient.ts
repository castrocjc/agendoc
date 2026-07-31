const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081";

interface ApiErrorResponse {
  timestamp?: string;
  status?: number;
  error?: string;
  message?: string;
  path?: string;
  code?: string;
}

export class ApiClientError extends Error {
  readonly status: number;
  readonly code?: string;
  readonly apiMessage?: string;

  constructor(
    message: string,
    status: number,
    code?: string,
    apiMessage?: string,
  ) {
    super(message);
    this.name = "ApiClientError";
    this.status = status;
    this.code = code;
    this.apiMessage = apiMessage;
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

async function readSuccessResponse<T>(response: Response): Promise<T> {
  if (response.status === 204) {
    return undefined as T;
  }

  try {
    return (await response.json()) as T;
  } catch {
    throw new ApiClientError(
      "La respuesta recibida no pudo procesarse correctamente.",
      500,
    );
  }
}

export async function apiRequest<T>(
  path: string,
  options: RequestInit = {},
): Promise<T> {
  let response: Response;

  try {
    response = await fetch(`${API_BASE_URL}${path}`, {
      ...options,
      headers: {
        Accept: "application/json",
        ...options.headers,
      },
    });
  } catch {
    throw new ApiClientError(
      "No fue posible conectarse con AgenDoc.",
      0,
    );
  }

  if (!response.ok) {
    const errorResponse = await readErrorResponse(response);

    throw new ApiClientError(
      errorResponse?.message ??
        "No fue posible completar la operación.",
      response.status,
      errorResponse?.code,
      errorResponse?.message,
    );
  }

  return readSuccessResponse<T>(response);
}

export function apiGet<T>(path: string): Promise<T> {
  return apiRequest<T>(path, {
    method: "GET",
  });
}

export function apiPost<TResponse, TRequest>(
  path: string,
  body: TRequest,
): Promise<TResponse> {
  return apiRequest<TResponse>(path, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
}

export function apiPatch<TResponse, TRequest>(
  path: string,
  body: TRequest,
): Promise<TResponse> {
  return apiRequest<TResponse>(path, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
}
