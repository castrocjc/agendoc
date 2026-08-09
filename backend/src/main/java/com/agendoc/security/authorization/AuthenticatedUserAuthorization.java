package com.agendoc.security.authorization;

import com.agendoc.security.context.AuthenticatedUserContext;
import com.agendoc.security.context.AuthenticatedUserContextProvider;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validates whether the authenticated user has one of the roles allowed
 * to execute a domain operation.
 */
@Component
@RequiredArgsConstructor
public class AuthenticatedUserAuthorization {

    private static final String ACCESS_DENIED_MESSAGE =
            "No tienes autorización para realizar esta operación.";

    private final AuthenticatedUserContextProvider authenticatedUserContextProvider;

    /**
     * Returns the authenticated context when the current role is allowed.
     *
     * @param allowedRoles roles allowed to execute the operation
     * @return authenticated user context
     */
    public AuthenticatedUserContext requireAnyRole(
            SecurityRoleCode... allowedRoles
    ) {

        AuthenticatedUserContext context =
                authenticatedUserContextProvider.getCurrentContext();

        SecurityRoleCode currentRole =
                SecurityRoleCode.from(context.roleCode());

        boolean allowed = Arrays.stream(allowedRoles)
                .anyMatch(role -> role == currentRole);

        if (!allowed) {
            throw new AuthorizationDeniedException(
                    ACCESS_DENIED_MESSAGE
            );
        }

        return context;
    }

    /**
     * Returns the authenticated context when the current role matches
     * the required role.
     *
     * @param requiredRole role required to execute the operation
     * @return authenticated user context
     */
    public AuthenticatedUserContext requireRole(
            SecurityRoleCode requiredRole
    ) {
        return requireAnyRole(requiredRole);
    }
}