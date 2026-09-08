package com.freshmarket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.freshmarket.service.UserSettingsService;
import java.util.Map;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/user/settings")
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    public UserSettingsController(UserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getUserSettings(
            Authentication authentication) {
        try {
            Map<String, Object> settings = userSettingsService.getUserSettings(authentication.getName());
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to fetch user settings"));
        }
    }

    @PostMapping("/language")
    public ResponseEntity<Map<String, String>> updateLanguage(
            Authentication authentication,
            @RequestBody Map<String, String> request) {
        try {
            String language = request.get("language");
            if (language == null || !isValidLanguage(language)) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid language code",
                    "validLanguages", "en, mr, hi"
                ));
            }

            userSettingsService.updateLanguage(authentication.getName(), language);
            return ResponseEntity.ok(Map.of(
                "message", "Language updated successfully",
                "language", language
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to update language"));
        }
    }

    private boolean isValidLanguage(String language) {
        return language.equals("en") || language.equals("mr") || language.equals("hi");
    }
} 