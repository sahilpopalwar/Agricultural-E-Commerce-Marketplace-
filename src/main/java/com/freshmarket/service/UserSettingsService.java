package com.freshmarket.service;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;

@Service
public class UserSettingsService {
    
    private final JdbcTemplate jdbcTemplate;

    public UserSettingsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private static final String GET_USER_SETTINGS_SQL = """
        SELECT u.user_id, u.email AS username, us.language, us.email_notifications, us.push_notifications
        FROM users u
        LEFT JOIN user_settings us ON u.user_id = us.user_id
        WHERE u.user_id = ?
    """;
    
    private static final String UPDATE_LANGUAGE_SQL = """
        INSERT INTO user_settings (user_id, language, updated_at)
        VALUES (?, ?, CURRENT_TIMESTAMP)
        ON DUPLICATE KEY UPDATE
        language = VALUES(language),
        updated_at = CURRENT_TIMESTAMP
    """;
    
    public Map<String, Object> getUserSettings(int userId) {
        try {
            return jdbcTemplate.queryForObject(GET_USER_SETTINGS_SQL, new UserSettingsRowMapper(), userId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user settings: " + e.getMessage(), e);
        }
    }

    public void updateLanguage(int userId, String language) {
        try {
            jdbcTemplate.update(UPDATE_LANGUAGE_SQL, userId, language);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update language: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getUserSettings(String identity) {
        Integer userId = findUserId(identity);
        return getUserSettings(userId);
    }

    public void updateLanguage(String identity, String language) {
        updateLanguage(findUserId(identity), language);
    }

    private Integer findUserId(String identity) {
        Integer id = jdbcTemplate.queryForObject(
                "SELECT user_id FROM users WHERE email = ? OR phone = ?",
                Integer.class, identity, identity);
        if (id == null) {
            throw new IllegalArgumentException("Authenticated user was not found");
        }
        return id;
    }
    
    private static class UserSettingsRowMapper implements RowMapper<Map<String, Object>> {
        @Override
        public Map<String, Object> mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Map<String, Object> settings = new HashMap<>();
            settings.put("userId", rs.getInt("user_id"));
            settings.put("userName", rs.getString("username"));
            settings.put("language", rs.getString("language"));
            settings.put("emailNotifications", rs.getBoolean("email_notifications"));
            settings.put("pushNotifications", rs.getBoolean("push_notifications"));
            return settings;
        }
    }
} 