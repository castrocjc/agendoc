package com.agendoc.security.context;

/**
 * Provides the domain context of the currently authenticated user.
 */
public interface AuthenticatedUserContextProvider {

    /**
     * Returns the context of the currently authenticated user.
     *
     * @return authenticated user context
     */
    AuthenticatedUserContext getCurrentContext();
}