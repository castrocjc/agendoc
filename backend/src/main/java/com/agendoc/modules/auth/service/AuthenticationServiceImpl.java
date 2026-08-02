package com.agendoc.modules.auth.service;

import com.agendoc.modules.auth.dto.AuthenticatedUser;
import com.agendoc.modules.auth.dto.LoginRequest;
import com.agendoc.modules.auth.dto.LoginResponse;
import com.agendoc.modules.auth.exception.InvalidCredentialsException;
import com.agendoc.modules.user.entity.UserEntity;
import com.agendoc.modules.user.repository.UserRepository;
import com.agendoc.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the authentication use case.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String TOKEN_TYPE = "Bearer";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    @Override
    public LoginResponse authenticate(LoginRequest request) {
        UserEntity user = findUser(request.identifier());

        validateUserStatus(user);
        validatePassword(request.password(), user.getPasswordHash());

        AuthenticatedUser authenticatedUser = createAuthenticatedUser(user);
        String accessToken = jwtTokenService.generateToken(authenticatedUser);

        return createResponse(
                accessToken,
                authenticatedUser
        );
    }

    private UserEntity findUser(String identifier) {

        return userRepository
                .findByUsernameIgnoreCaseOrEmailIgnoreCase(
                        identifier,
                        identifier
                )
                .orElseThrow(InvalidCredentialsException::new);

    }

    private void validateUserStatus(UserEntity user) {
        if (!user.isActive()) {
            throw new InvalidCredentialsException();
        }
    }

    private void validatePassword(
            String rawPassword,
            String passwordHash
    ) {
        if (!passwordEncoder.matches(rawPassword, passwordHash)) {
            throw new InvalidCredentialsException();
        }
    }

    private AuthenticatedUser createAuthenticatedUser(UserEntity user) {

        return new AuthenticatedUser(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().getCode(),
                user.getClinic().getId()
        );

    }

    private LoginResponse createResponse(
            String accessToken,
            AuthenticatedUser authenticatedUser
    ) {
        return new LoginResponse(
                accessToken,
                TOKEN_TYPE,
                jwtTokenService.getExpirationSeconds(),
                authenticatedUser
        );
    }
}