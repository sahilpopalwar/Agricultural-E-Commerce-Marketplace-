package com.freshmarket.service;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class LanguageService {
    private static final Map<String, Map<String, String>> translationCache = new ConcurrentHashMap<>();
    private static final Map<String, Long> cacheTimestamps = new ConcurrentHashMap<>();
    private static final long CACHE_DURATION = TimeUnit.HOURS.toMillis(1);
    
    private final JdbcTemplate jdbcTemplate;

    public LanguageService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private static final String GET_TRANSLATIONS_SQL = """
        SELECT t.key, t.value
        FROM translations t
        WHERE t.language = ?
    """;
    
    private static final String GET_TRANSLATION_SQL = """
        SELECT t.value
        FROM translations t
        WHERE t.language = ? AND t.key = ?
    """;
    
    public String getTranslation(String key, String language) {
        validateLanguage(language);
        
        // Check cache first
        Map<String, String> cachedTranslations = getCachedTranslations(language);
        if (cachedTranslations.containsKey(key)) {
            return cachedTranslations.get(key);
        }
        
        // If not in cache, fetch from database
        try {
            String translation = jdbcTemplate.queryForObject(
                GET_TRANSLATION_SQL, 
                String.class, 
                language, 
                key
            );
            
            if (translation != null) {
                // Update cache
                cachedTranslations.put(key, translation);
                return translation;
            }
            
            // If translation not found, return key as fallback
            return key;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get translation: " + e.getMessage(), e);
        }
    }
    
    public Map<String, String> getAllTranslations(String language) {
        validateLanguage(language);
        
        // Check cache first
        Map<String, String> cachedTranslations = getCachedTranslations(language);
        if (!cachedTranslations.isEmpty()) {
            return new HashMap<>(cachedTranslations);
        }
        
        // If not in cache, fetch from database
        try {
            Map<String, String> translations = new HashMap<>();
            jdbcTemplate.query(GET_TRANSLATIONS_SQL, new TranslationRowMapper(translations), language);
            
            // Update cache
            translationCache.put(language, translations);
            cacheTimestamps.put(language, System.currentTimeMillis());
            
            return new HashMap<>(translations);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get translations: " + e.getMessage(), e);
        }
    }
    
    private Map<String, String> getCachedTranslations(String language) {
        Long timestamp = cacheTimestamps.get(language);
        if (timestamp != null && System.currentTimeMillis() - timestamp < CACHE_DURATION) {
            return translationCache.getOrDefault(language, new HashMap<>());
        }
        return new HashMap<>();
    }
    
    private void validateLanguage(String language) {
        if (language == null || !isValidLanguage(language)) {
            throw new IllegalArgumentException("Invalid language code: " + language);
        }
    }
    
    private boolean isValidLanguage(String language) {
        return language.equals("en") || language.equals("mr") || language.equals("hi");
    }
    
    private static class TranslationRowMapper implements RowMapper<Void> {
        private final Map<String, String> translations;
        
        public TranslationRowMapper(Map<String, String> translations) {
            this.translations = translations;
        }
        
        @Override
        public Void mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            translations.put(rs.getString("key"), rs.getString("value"));
            return null;
        }
    }
} 