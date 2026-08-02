export type UserRole =
  | "PACIENTE"
  | "RECEPCIONISTA"
  | "MEDICO";

export interface LoginRequest {
  identifier: string;
  password: string;
}

export interface AuthenticatedUser {
  id: number;
  username: string;
  email: string;
  role: UserRole;
  clinicId: number;
}

export interface LoginResponse {
  accessToken: string;
  tokenType: "Bearer";
  expiresIn: number;
  user: AuthenticatedUser;
}