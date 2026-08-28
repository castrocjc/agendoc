package com.agendoc.security.authorization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agendoc.security.context.AuthenticatedUserContext;
import com.agendoc.security.context.AuthenticatedUserContextProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthenticatedUserAuthorizationTest {

    private AuthenticatedUserContextProvider contextProvider;
    private AuthenticatedUserAuthorization authorization;

    @BeforeEach
    void setUp() {
        contextProvider = mock(
                AuthenticatedUserContextProvider.class
        );

        authorization = new AuthenticatedUserAuthorization(
                contextProvider
        );
    }

    @Test
    void shouldAuthorizeRequiredRole() {

        AuthenticatedUserContext context =
                createContext("RECEPTIONIST");

        when(contextProvider.getCurrentContext())
                .thenReturn(context);

        AuthenticatedUserContext result =
                authorization.requireRole(
                        SecurityRoleCode.RECEPTIONIST
                );

        assertSame(context, result);

        verify(contextProvider)
                .getCurrentContext();
    }

    @Test
    void shouldAuthorizeAnyAllowedRole() {

        AuthenticatedUserContext context =
                createContext("PATIENT");

        when(contextProvider.getCurrentContext())
                .thenReturn(context);

        AuthenticatedUserContext result =
                authorization.requireAnyRole(
                        SecurityRoleCode.PATIENT,
                        SecurityRoleCode.RECEPTIONIST
                );

        assertSame(context, result);
    }

    @Test
    void shouldRejectRoleNotAllowed() {

        AuthenticatedUserContext context =
                createContext("DOCTOR");

        when(contextProvider.getCurrentContext())
                .thenReturn(context);

        AuthorizationDeniedException exception =
                assertThrows(
                        AuthorizationDeniedException.class,
                        () -> authorization.requireAnyRole(
                                SecurityRoleCode.PATIENT,
                                SecurityRoleCode.RECEPTIONIST
                        )
                );

        assertEquals(
                "No tienes autorización para realizar esta operación.",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectUnsupportedContextRole() {

        AuthenticatedUserContext context =
                createContext("UNSUPPORTED_ROLE");

        when(contextProvider.getCurrentContext())
                .thenReturn(context);

        assertThrows(
                IllegalStateException.class,
                () -> authorization.requireRole(
                        SecurityRoleCode.RECEPTIONIST
                )
        );
    }

    private AuthenticatedUserContext createContext(
            String roleCode
    ) {
        return new AuthenticatedUserContext(
                1L,
                "authenticated.user",
                1L,
                roleCode,
                null,
                null
        );
    }
}