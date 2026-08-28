import type {
  AuthenticatedUser,
  LoginResponse,
  UserRole,
} from "../types/auth.types";

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

export function getAuthenticatedUser(): AuthenticatedUser | null {
  return getSession()?.user ?? null;
}

export function getAccessToken(): string | null {
  return getSession()?.accessToken ?? null;
}

export function getUserRole(): UserRole | null {
  return getAuthenticatedUser()?.role ?? null;
}

export function getDefaultRouteForRole(role: UserRole): string {
  switch (role) {
    case "ADMIN":
      return "/admin";

    case "RECEPTIONIST":
      return "/dashboard";

    case "PATIENT":
    case "DOCTOR":
      return "/account";
  }
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
