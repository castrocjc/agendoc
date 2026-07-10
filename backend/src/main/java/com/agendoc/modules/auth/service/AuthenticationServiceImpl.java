package com.agendoc.modules.auth.service;

import com.agendoc.modules.auth.dto.AuthenticatedUser;
import com.agendoc.modules.auth.dto.LoginRequest;
import com.agendoc.modules.auth.dto.LoginResponse;
import com.agendoc.modules.user.entity.UserEntity;
import com.agendoc.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the authentication use case.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse authenticate(LoginRequest request) {
        UserEntity user = findUser(request.identifier());

        validateUserStatus(user);
        validatePassword(request.password(), user.getPasswordHash());

        AuthenticatedUser authenticatedUser = createAuthenticatedUser(user);

        return createResponse(authenticatedUser);
    }

    private UserEntity findUser(String identifier) {
        throw new UnsupportedOperationException(
                "User lookup is not implemented yet."
        );
    }

    private void validateUserStatus(UserEntity user) {
        throw new UnsupportedOperationException(
                "User status validation is not implemented yet."
        );
    }

    private void validatePassword(
            String rawPassword,
            String passwordHash
    ) {
        throw new UnsupportedOperationException(
                "Password validation is not implemented yet."
        );
    }

    private AuthenticatedUser createAuthenticatedUser(UserEntity user) {
        throw new UnsupportedOperationException(
                "Authenticated user mapping is not implemented yet."
        );
    }

    private LoginResponse createResponse(
            AuthenticatedUser authenticatedUser
    ) {
        throw new UnsupportedOperationException(
                "Login response creation is not implemented yet."
        );
    }
}