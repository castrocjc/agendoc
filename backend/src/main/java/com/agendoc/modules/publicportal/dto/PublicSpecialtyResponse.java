package com.agendoc.modules.publicportal.dto;

/**
 * Medical specialty exposed through the Digital Reception.
 */
public record PublicSpecialtyResponse(
        Long id,
        String name
) {
}