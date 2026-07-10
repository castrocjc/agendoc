import type { LoginResponse } from "../types/auth.types";

const SESSION_STORAGE_KEY = "agendoc_session";

export function saveSession(session: LoginResponse): void {
  localStorage.setItem(SESSION_STORAGE_KEY, JSON.stringify(session));
}

export function getSession(): LoginResponse | null {
  const storedSession = localStorage.getItem(SESSION_STORAGE_KEY);

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

export function getToken(): string | null {
  return getSession()?.token ?? null;
}

export function isAuthenticated(): boolean {
  return Boolean(getToken());
}

export function clearSession(): void {
  localStorage.removeItem(SESSION_STORAGE_KEY);
}