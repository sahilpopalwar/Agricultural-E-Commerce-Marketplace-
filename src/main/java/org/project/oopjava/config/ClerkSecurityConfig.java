package org.project.oopjava.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@ConditionalOnProperty(name = "app.clerk.enabled", havingValue = "true")
public class ClerkSecurityConfig {

    @Bean
    ClerkProperties clerkProperties(
            @org.springframework.beans.factory.annotation.Value("${app.clerk.jwks-url}") String jwksUrl) {
        if (jwksUrl.isBlank()) {
            throw new IllegalStateException(
                "CLERK_JWKS_URL must be set when CLERK_ENABLED=true.");
        }
        return new ClerkProperties(jwksUrl);
    }

    @Bean
    JwtDecoder clerkJwtDecoder(ClerkProperties properties) {
        return NimbusJwtDecoder.withJwkSetUri(properties.jwksUrl()).build();
    }

    @Bean
    JwtAuthenticationConverter clerkJwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("SCOPE_");
        authoritiesConverter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    @Bean
    @Order(1)
    SecurityFilterChain clerkApiSecurityFilterChain(
            HttpSecurity http,
            JwtDecoder clerkJwtDecoder,
            JwtAuthenticationConverter clerkJwtAuthenticationConverter) throws Exception {
        http
            .securityMatcher("/api/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(clerkJwtDecoder)
                    .jwtAuthenticationConverter(clerkJwtAuthenticationConverter)
                )
            );
        return http.build();
    }

    record ClerkProperties(String jwksUrl) {}
}
