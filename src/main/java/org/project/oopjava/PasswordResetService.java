package org.project.oopjava;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;

import org.project.oopjava.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordResetService {
    private static final Duration TOKEN_LIFETIME = Duration.ofMinutes(30);
    private final JdbcTemplate jdbcTemplate;
    private final ObjectProvider<JavaMailSender> mailSender;
    private final boolean deliveryEnabled;
    private final SecureRandom random = new SecureRandom();

    public PasswordResetService(
            JdbcTemplate jdbcTemplate,
            ObjectProvider<JavaMailSender> mailSender,
            @Value("${app.password-reset.delivery-enabled:false}") boolean deliveryEnabled) {
        this.jdbcTemplate = jdbcTemplate;
        this.mailSender = mailSender;
        this.deliveryEnabled = deliveryEnabled;
    }

    @Transactional
    public String createToken(String email) {
        String normalized = email == null ? "" : email.trim().toLowerCase();
        if (!normalized.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        Integer userId;
        try {
            userId = jdbcTemplate.queryForObject(
                    "SELECT user_id FROM users WHERE email = ?", Integer.class, normalized);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
        if (userId == null) {
            throw new IllegalArgumentException("Account not found");
        }
        jdbcTemplate.update("DELETE FROM password_reset_tokens WHERE user_id = ? OR expires_at < CURRENT_TIMESTAMP", userId);
        String token = newToken();
        jdbcTemplate.update("""
                INSERT INTO password_reset_tokens (user_id, token_hash, expires_at)
                VALUES (?, ?, ?)
                """, userId, hash(token), Timestamp.from(Instant.now().plus(TOKEN_LIFETIME)));
        return token;
    }

    public void deliver(String email, String token) {
        if (!deliveryEnabled) {
            jdbcTemplate.update("DELETE FROM password_reset_tokens WHERE token_hash = ?", hash(token));
            throw new PasswordResetUnavailableException();
        }
        JavaMailSender sender = mailSender.getIfAvailable();
        if (sender == null) {
            throw new PasswordResetUnavailableException();
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Fresh Market password reset");
        message.setText("Use this one-time reset token within 30 minutes: " + token
                + "\nSubmit it at the password reset page.");
        try {
            sender.send(message);
        } catch (RuntimeException exception) {
            jdbcTemplate.update("DELETE FROM password_reset_tokens WHERE token_hash = ?", hash(token));
            throw new PasswordResetUnavailableException();
        }
    }

    @Transactional
    public boolean reset(String token, String newPassword) {
        if (!SecurityUtils.isValidPassword(newPassword) || token == null || token.length() < 32) {
            return false;
        }
        int updated = jdbcTemplate.update("""
                UPDATE users u JOIN password_reset_tokens t ON t.user_id = u.user_id
                SET u.password_hash = ?, t.used_at = CURRENT_TIMESTAMP
                WHERE t.token_hash = ? AND t.used_at IS NULL AND t.expires_at > CURRENT_TIMESTAMP
                """, SecurityUtils.encodePassword(newPassword), hash(token));
        return updated == 1;
    }

    private String newToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    private String hash(String token) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (java.security.NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Hash algorithm unavailable", ex);
        }
    }
}
