package com.agendoc.security.config;

import java.util.List;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;

/**
 * Configures HTTP security rules for the AgenDoc API.
 */
@Configuration
public class SecurityConfiguration {

        private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";
        private static final String DOCTORS_ENDPOINT = "/api/v1/doctors";
        private static final String MEDICAL_SPECIALTIES_ENDPOINT = "/api/v1/medical-specialties";

        private final boolean permitDoctorEndpoints;

        public SecurityConfiguration(
                @Value("${agendoc.security.permit-doctor-endpoints:false}")
                boolean permitDoctorEndpoints
        ) {
        this.permitDoctorEndpoints = permitDoctorEndpoints;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http) throws Exception {

                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(
                                                                SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(authorize -> {

                                authorize
                                        .requestMatchers(
                                                HttpMethod.POST,
                                                LOGIN_ENDPOINT
                                        )
                                        .permitAll();

                                if (permitDoctorEndpoints) {
                                        authorize
                                                .requestMatchers(
                                                        HttpMethod.GET,
                                                        MEDICAL_SPECIALTIES_ENDPOINT
                                                )
                                                .permitAll()
                                                .requestMatchers(
                                                        HttpMethod.POST,
                                                        DOCTORS_ENDPOINT
                                                )
                                                .permitAll();
                                }

                                authorize.anyRequest().authenticated();
                                })
                                .formLogin(form -> form.disable())
                                .httpBasic(basic -> basic.disable());

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(List.of(
                                "http://localhost:5173",
                                "http://127.0.0.1:5173"));

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

                source.registerCorsConfiguration("/**", configuration);

                return source;
        }
}