package com.agendoc.modules.publicportal.dto;

/**
 * Public information displayed in the Digital Reception of a clinic.
 */
public record PublicClinicResponse(
        String slug,
        String publicName,
        String publicDescription,
        String logoUrl,
        String phone,
        String whatsapp,
        String email,
        String address,
        String mapUrl
) {
}