export type UserRole = "PACIENTE" | "RECEPCIONISTA" | "MEDICO";

export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthenticatedUser {
  id: number;
  username: string;
  email: string;
  role: UserRole;
  consultorioId: number;
}

export interface LoginResponse {
  token: string;
  tokenType: "Bearer";
  user: AuthenticatedUser;
}