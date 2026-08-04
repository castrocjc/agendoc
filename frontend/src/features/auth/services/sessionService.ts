import type { LoginResponse } from "../types/auth.types";

const SESSION_STORAGE_KEY = "agendoc.auth.session";
const SESSION_CLEARED_EVENT = "agendoc.auth.session-cleared";

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
  const hadSession =
    sessionStorage.getItem(SESSION_STORAGE_KEY) !== null;

  sessionStorage.removeItem(SESSION_STORAGE_KEY);

  if (hadSession) {
    window.dispatchEvent(
      new Event(SESSION_CLEARED_EVENT),
    );
  }
}

export function isAuthenticated(): boolean {
  return getSession() !== null;
}

export function subscribeToSessionCleared(
  listener: () => void,
): () => void {
  window.addEventListener(
    SESSION_CLEARED_EVENT,
    listener,
  );

  return () => {
    window.removeEventListener(
      SESSION_CLEARED_EVENT,
      listener,
    );
  };
}
