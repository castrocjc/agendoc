package com.agendoc.security.authorization;

import java.util.Locale;

/**
 * Technical role codes supported by AgenDoc authorization policies.
 */
public enum SecurityRoleCode {

    PATIENT,
    RECEPTIONIST,
    DOCTOR;

    /**
     * Converts a persisted role code into its supported typed representation.
     *
     * @param roleCode persisted technical role code
     * @return supported security role
     */
    public static SecurityRoleCode from(String roleCode) {

        if (roleCode == null || roleCode.isBlank()) {
            throw new IllegalStateException(
                    "Authenticated user does not have a valid role code"
            );
        }

        try {
            return SecurityRoleCode.valueOf(
                    roleCode.trim().toUpperCase(Locale.ROOT)
            );
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "Authenticated user has an unsupported role",
                    exception
            );
        }
    }
}