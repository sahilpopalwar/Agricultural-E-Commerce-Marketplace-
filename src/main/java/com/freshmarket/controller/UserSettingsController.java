package com.freshmarket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.freshmarket.service.UserSettingsService;
import java.util.Map;

@RestController
@RequestMapping("/api/user/settings")
@CrossOrigin(origins = "http://localhost:8080")
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    public UserSettingsController(UserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getUserSettings(
            @RequestHeader(value = "X-User-ID", required = false, defaultValue = "1") int userId) {
        try {
            Map<String, Object> settings = userSettingsService.getUserSettings(userId);
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to fetch user settings",
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/language")
    public ResponseEntity<Map<String, String>> updateLanguage(
            @RequestHeader(value = "X-User-ID", required = false, defaultValue = "1") int userId,
            @RequestBody Map<String, String> request) {
        try {
            String language = request.get("language");
            if (language == null || !isValidLanguage(language)) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid language code",
                    "validLanguages", "en, mr, hi"
                ));
            }

            userSettingsService.updateLanguage(userId, language);
            return ResponseEntity.ok(Map.of(
                "message", "Language updated successfully",
                "language", language
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to update language",
                "message", e.getMessage()
            ));
        }
    }

    private boolean isValidLanguage(String language) {
        return language.equals("en") || language.equals("mr") || language.equals("hi");
    }
} 