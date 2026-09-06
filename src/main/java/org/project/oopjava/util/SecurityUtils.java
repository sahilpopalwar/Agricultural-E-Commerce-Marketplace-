package org.project.oopjava.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.util.HtmlUtils;

import java.util.regex.Pattern;

public class SecurityUtils {

    private static final Pattern XSS_PATTERN = Pattern.compile(
        "<script.*?>.*?</script>|javascript:|on\\w+\\s*=|data:text/html|<.*?\\son\\w+\\s*=",
        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL
    );

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @NonNull
    public static String encodePassword(@NonNull String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public static boolean matchesPassword(@NonNull String rawPassword, @NonNull String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @NonNull
    public static String sanitizeInput(@Nullable String input) {
        if (input == null) {
            return "";
        }
        // Remove XSS patterns
        String sanitized = XSS_PATTERN.matcher(input).replaceAll("");
        // HTML escape
        return HtmlUtils.htmlEscape(sanitized);
    }

    // Returns null if no authenticated user — callers must null-check
    @Nullable
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public static boolean hasRole(@NonNull String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
               authentication.getAuthorities().stream()
                   .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    @NonNull
    public static String generateSecureToken() {
        return java.util.UUID.randomUUID().toString();
    }

    public static boolean isValidPassword(@Nullable String password) {
        // Password must be at least 8 characters long
        // and contain at least one uppercase letter, one lowercase letter,
        // one number, and one special character
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password != null && password.matches(passwordPattern);
    }
}