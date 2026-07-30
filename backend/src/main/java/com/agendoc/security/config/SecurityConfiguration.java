package com.agendoc.security.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configures HTTP security rules for the AgenDoc API.
 */
@Configuration
public class SecurityConfiguration {
        private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";
        private static final String DOCTORS_ENDPOINT = "/api/v1/doctors";
        private static final String MEDICAL_SPECIALTIES_ENDPOINT = "/api/v1/medical-specialties";
        private static final String PATIENTS_ENDPOINT = "/api/v1/patients";
        private static final String PATIENT_SEARCH_ENDPOINT = "/api/v1/patients/search";
        private static final String AGENDA_BLOCKS_ENDPOINT = "/api/v1/doctors/*/agenda-blocks";
        private static final String APPOINTMENTS_ENDPOINT = "/api/v1/appointments";        
        private final boolean permitDevelopmentEndpoints;

        public SecurityConfiguration(
                        @Value("${agendoc.security.permit-development-endpoints:false}") boolean permitDevelopmentEndpoints) {
                this.permitDevelopmentEndpoints = permitDevelopmentEndpoints;
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
                                .authorizeHttpRequests(authorize -> {
                                        authorize
                                                        .requestMatchers(
                                                                        HttpMethod.POST,
                                                                        LOGIN_ENDPOINT)
                                                        .permitAll();

                                        if (permitDevelopmentEndpoints) {
                                                authorize
                                                                .requestMatchers(
                                                                                HttpMethod.GET,
                                                                                MEDICAL_SPECIALTIES_ENDPOINT)
                                                                .permitAll()
                                                                .requestMatchers(
                                                                        HttpMethod.GET,
                                                                        DOCTORS_ENDPOINT)
                                                                .permitAll()
                                                                .requestMatchers(
                                                                                HttpMethod.POST,
                                                                                DOCTORS_ENDPOINT)
                                                                .permitAll()
                                                                .requestMatchers(
                                                                                HttpMethod.POST,
                                                                                PATIENTS_ENDPOINT)
                                                                .permitAll()
                                                                .requestMatchers(
                                                                                HttpMethod.GET,
                                                                                PATIENT_SEARCH_ENDPOINT)
                                                                .permitAll()
                                                                .requestMatchers(
                                                                        HttpMethod.POST,
                                                                        AGENDA_BLOCKS_ENDPOINT)
                                                                .permitAll()
                                                                .requestMatchers(
                                                                        HttpMethod.GET,
                                                                        AGENDA_BLOCKS_ENDPOINT)
                                                                .permitAll()
                                                                .requestMatchers(
                                                                        HttpMethod.POST,
                                                                        APPOINTMENTS_ENDPOINT)
                                                                .permitAll()
                                                                .requestMatchers(
                                                                        HttpMethod.GET,
                                                                        APPOINTMENTS_ENDPOINT)
                                                                .permitAll();                                                            
                                        }
                                        authorize
                                                        .anyRequest()
                                                        .authenticated();
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

                source.registerCorsConfiguration(
                                "/**",
                                configuration);

                return source;
        }
}