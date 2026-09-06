package org.project.oopjava.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.dao.EmptyResultDataAccessException;

@Controller
@RequestMapping("/login")
public class OtpLoginController {
    private static final Duration OTP_VALIDITY = Duration.ofMinutes(5);

    private final JdbcTemplate jdbcTemplate;
    private final Msg91OtpService otpService;
    private final SecurityContextRepository securityContextRepository =
        new HttpSessionSecurityContextRepository();

    public OtpLoginController(JdbcTemplate jdbcTemplate, Msg91OtpService otpService) {
        this.jdbcTemplate = jdbcTemplate;
        this.otpService = otpService;
    }

    @PostMapping("/request-otp")
    public String requestOtp(String phone, Model model) {
        String normalizedPhone = normalizePhone(phone);
        if (normalizedPhone == null) {
            model.addAttribute("error", "Enter a valid mobile number.");
            return "login";
        }

        String otp = String.format("%06d", new java.security.SecureRandom().nextInt(1_000_000));
        try {
            otpService.send(normalizedPhone, otp);
        } catch (OtpDeliveryException ex) {
            model.addAttribute("error", ex.getMessage());
            return "login";
        }
        Instant expiresAt = Instant.now().plus(OTP_VALIDITY);
        jdbcTemplate.update(
            "INSERT INTO otp_verifications (phone, otp_hash, expires_at) VALUES (?, ?, ?)",
            normalizedPhone, hashOtp(otp), Timestamp.from(expiresAt));

        model.addAttribute("phone", normalizedPhone);
        model.addAttribute("otpSent", true);
        return "login";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(String phone, String otp, HttpServletRequest request,
                            HttpServletResponse response, Model model) {
        String normalizedPhone = normalizePhone(phone);
        if (normalizedPhone == null || otp == null || !otp.matches("\\d{6}")
                || !verifyAndConsumeOtp(normalizedPhone, otp)) {
            model.addAttribute("error", "Invalid or expired OTP.");
            model.addAttribute("phone", phone);
            model.addAttribute("otpSent", true);
            return "login";
        }

        String role = findRole(normalizedPhone);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            normalizedPhone, null, java.util.List.of(new SimpleGrantedAuthority("ROLE_" + role)));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextRepository.saveContext(context, request, response);
        return "redirect:/";
    }

    private boolean verifyAndConsumeOtp(String phone, String otp) {
        String otpHash = hashOtp(otp);
        int updated = jdbcTemplate.update("""
            UPDATE otp_verifications
            SET used_at = CURRENT_TIMESTAMP
            WHERE phone = ?
              AND otp_hash = ?
              AND used_at IS NULL
              AND expires_at > CURRENT_TIMESTAMP
            ORDER BY created_at DESC
            LIMIT 1
            """, phone, otpHash);
        return updated == 1;
    }

    private String hashOtp(String otp) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                .digest(otp.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder(64);
            for (byte value : digest) {
                hash.append(String.format("%02x", value));
            }
            return hash.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is unavailable", ex);
        }
    }

    private String findRole(String phone) {
        try {
            String role = jdbcTemplate.queryForObject(
                "SELECT role FROM users WHERE phone = ?", String.class, phone);
            return role == null ? "USER" : role.replace("ROLE_", "").toUpperCase();
        } catch (EmptyResultDataAccessException ex) {
            return "USER";
        }
    }

    private String normalizePhone(String phone) {
        if (phone == null) {
            return null;
        }
        String digits = phone.replaceAll("\\D", "");
        return digits.length() >= 10 && digits.length() <= 15 ? digits : null;
    }
}
