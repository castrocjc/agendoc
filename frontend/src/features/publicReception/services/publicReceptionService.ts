import {
  apiGet,
  apiPost,
} from "../../../shared/api/apiClient";

import type {
  FirstAppointmentRequest,
  FirstAppointmentResponse,
  PublicAgendaAvailability,
  PublicClinic,
  PublicDoctorSummary,
  PublicSpecialty,
} from "../types/publicReception.types";

function buildClinicPath(clinicSlug: string): string {
  return `/api/v1/public/clinics/${encodeURIComponent(clinicSlug)}`;
}

export function getPublicClinic(
  clinicSlug: string,
): Promise<PublicClinic> {
  return apiGet<PublicClinic>(
    buildClinicPath(clinicSlug),
  );
}

export function getPublicSpecialties(
  clinicSlug: string,
): Promise<PublicSpecialty[]> {
  return apiGet<PublicSpecialty[]>(
    `${buildClinicPath(clinicSlug)}/specialties`,
  );
}

export function getPublicDoctors(
  clinicSlug: string,
  specialtyId?: number,
): Promise<PublicDoctorSummary[]> {
  const query = specialtyId === undefined
    ? ""
    : `?specialtyId=${encodeURIComponent(String(specialtyId))}`;

  return apiGet<PublicDoctorSummary[]>(
    `${buildClinicPath(clinicSlug)}/doctors${query}`,
  );
}

export function getPublicAvailability(
  clinicSlug: string,
  doctorId: number,
  date: string,
): Promise<PublicAgendaAvailability[]> {
  const query = new URLSearchParams({
    date,
  });

  return apiGet<PublicAgendaAvailability[]>(
    `${buildClinicPath(clinicSlug)}/doctors/${doctorId}/availability?${query.toString()}`,
  );
}

export function createFirstAppointment(
  clinicSlug: string,
  request: FirstAppointmentRequest,
): Promise<FirstAppointmentResponse> {
  return apiPost<
    FirstAppointmentResponse,
    FirstAppointmentRequest
  >(
    `${buildClinicPath(clinicSlug)}/first-appointments`,
    request,
  );
}
