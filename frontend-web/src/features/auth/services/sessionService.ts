import type { LoginResponse } from "../types/auth.types";

let currentSession: LoginResponse | null = null;

export function saveSession(session: LoginResponse): void {
  currentSession = session;
}

export function getSession(): LoginResponse | null {
  return currentSession;
}

export function clearSession(): void {
  currentSession = null;
}

export function isAuthenticated(): boolean {
  return currentSession !== null;
}