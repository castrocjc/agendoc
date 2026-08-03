package com.agendoc.security.config;

import java.util.List;
import com.agendoc.security.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configures HTTP security rules for the AgenDoc API.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
        private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";
        private final List<String> allowedOrigins;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfiguration(
                @Value("${agendoc.cors.allowed-origins}")
                List<String> allowedOrigins,
                JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.allowedOrigins = List.copyOf(allowedOrigins);
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http) throws Exception {
                http
                                .cors(cors -> cors.configurationSource(
                                                corsConfigurationSource()))
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))
                                .exceptionHandling(exception -> exception
                                        .authenticationEntryPoint(
                                                (request, response, authenticationException) ->
                                                        response.sendError(
                                                                HttpServletResponse.SC_UNAUTHORIZED
                                                        )
                                        )
                                )
                                .authorizeHttpRequests(authorize -> {
                                        authorize
                                                        .requestMatchers(
                                                                        HttpMethod.POST,
                                                                        LOGIN_ENDPOINT)
                                                        .permitAll();
                                        authorize
                                                        .anyRequest()
                                                        .authenticated();
                                })
                                .formLogin(form -> form.disable())
                                .httpBasic(basic -> basic.disable())
                                .addFilterBefore(
                                        jwtAuthenticationFilter,
                                        UsernamePasswordAuthenticationFilter.class
                                );

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(allowedOrigins);

                configuration.setAllowedMethods(List.of(
                                "GET",
                                "POST",
                                "PUT",
                                "PATCH",
                                "DELETE",
                                "OPTIONS"));

                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration(
                                "/**",
                                configuration);

                return source;
        }
}