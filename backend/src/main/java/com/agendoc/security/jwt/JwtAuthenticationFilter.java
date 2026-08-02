package com.agendoc.security.jwt;

import com.agendoc.modules.user.entity.UserEntity;
import com.agendoc.modules.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Authenticates requests based on a JWT access token.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader == null
                || !authorizationHeader.startsWith(TOKEN_PREFIX)) {

            filterChain.doFilter(request, response);
            return;

        }

        String token =
                authorizationHeader.substring(TOKEN_PREFIX.length());

        if (!jwtTokenService.isTokenValid(token)) {

            filterChain.doFilter(request, response);
            return;

        }

        Long userId = jwtTokenService.extractUserId(token);

        userRepository
                .findWithClinicAndRoleById(userId)
                .filter(UserEntity::isActive)
                .ifPresent(this::authenticate);

        filterChain.doFilter(request, response);

    }

    private void authenticate(UserEntity user) {

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_" + user.getRole().getCode()
                                )
                        )
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

    }

}