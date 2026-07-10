package com.agendoc.modules.auth.service;

import com.agendoc.modules.auth.dto.LoginRequest;
import com.agendoc.modules.auth.dto.LoginResponse;

/**
 * Defines the authentication use case.
 */
public interface AuthenticationService {

    LoginResponse authenticate(LoginRequest request);
}