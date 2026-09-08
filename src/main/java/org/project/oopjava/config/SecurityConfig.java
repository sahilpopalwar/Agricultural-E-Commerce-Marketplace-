package org.project.oopjava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.core.annotation.Order;

@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ── URL authorisation ──────────────────────────────────────────
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",
                    "/farmer-register",
                    "/services",
                    "/about",
                    "/contact",
                    "/project",
                    "/error",
                    "/favicon.ico",
                    "/forget-password",
                    "/forgot-password",
                    "/login/**",
                    "/clerk-login",
                    "/css/**",
                    "/js/**",
                    "/frontend/**",
                    "/resurces/**",
                    "/sahil.jpg",
                    "/profile.jpg"
                ).permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/farmer/**").hasRole("FARMER")
                .anyRequest().authenticated()
            )

            // ── Login ──────────────────────────────────────────────────────
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            )

            // ── Remember-me (replaces spring.security.remember-me.* props) ─
            .rememberMe(remember -> remember
                .key("your-secure-key-change-in-production")
                .tokenValiditySeconds(86400)   // 24 hours
            )

            // ── CSRF: keep Spring Security's default session-based CSRF ────
            .csrf(Customizer.withDefaults())

            // ── Security headers (replaces security.headers.* props) ───────
            .headers(headers -> headers
                // HSTS — max-age 1 year, include sub-domains
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000)
                )
                // X-Frame-Options: DENY
                .frameOptions(frame -> frame.deny())
                // X-Content-Type-Options: nosniff (enabled by default)
                .contentTypeOptions(Customizer.withDefaults())
                // Content-Security-Policy
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives(
                        "default-src 'self'; " +
                        "script-src 'self' https://cdn.jsdelivr.net 'unsafe-inline'; " +
                        "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                        "font-src 'self' https://fonts.gstatic.com; " +
                        "connect-src 'self' https://*.clerk.accounts.dev https://api.clerk.com; " +
                        "frame-src 'self' https://*.clerk.accounts.dev"
                    )
                )
                // X-XSS-Protection is obsolete in modern browsers;
                // rely on CSP above instead of the old header
                .xssProtection(xss -> xss.disable())
            );

        return http.build();
    }
}