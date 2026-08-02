import type { LoginResponse } from "../types/auth.types";

const SESSION_STORAGE_KEY = "agendoc.auth.session";

export function saveSession(session: LoginResponse): void {
  sessionStorage.setItem(
    SESSION_STORAGE_KEY,
    JSON.stringify(session),
  );
}

export function getSession(): LoginResponse | null {
  const storedSession = sessionStorage.getItem(SESSION_STORAGE_KEY);

  if (!storedSession) {
    return null;
  }

  try {
    return JSON.parse(storedSession) as LoginResponse;
  } catch {
    clearSession();
    return null;
  }
}

export function getAccessToken(): string | null {
  const session = getSession();

  if (!session?.accessToken) {
    return null;
  }

  return session.accessToken;
}

export function clearSession(): void {
  sessionStorage.removeItem(SESSION_STORAGE_KEY);
}

export function isAuthenticated(): boolean {
  return getSession() !== null;
}