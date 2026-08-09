package com.agendoc.security.authorization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SecurityRoleCodeTest {

    @Test
    void shouldConvertSupportedRoleCode() {

        SecurityRoleCode role =
                SecurityRoleCode.from("RECEPTIONIST");

        assertEquals(
                SecurityRoleCode.RECEPTIONIST,
                role
        );
    }

    @Test
    void shouldNormalizeRoleCode() {

        SecurityRoleCode role =
                SecurityRoleCode.from(" patient ");

        assertEquals(
                SecurityRoleCode.PATIENT,
                role
        );
    }

    @Test
    void shouldRejectNullRoleCode() {

        assertThrows(
                IllegalStateException.class,
                () -> SecurityRoleCode.from(null)
        );
    }

    @Test
    void shouldRejectBlankRoleCode() {

        assertThrows(
                IllegalStateException.class,
                () -> SecurityRoleCode.from(" ")
        );
    }

    @Test
    void shouldRejectUnsupportedRoleCode() {

        assertThrows(
                IllegalStateException.class,
                () -> SecurityRoleCode.from("ADMIN")
        );
    }
}