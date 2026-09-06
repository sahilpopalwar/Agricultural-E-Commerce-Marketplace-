package com.freshmarket.controller;

import com.freshmarket.service.LanguageService;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/language")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000"})
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping("/translations/{language}")
    public ResponseEntity<?> getTranslations(@PathVariable String language) {
        try {
            if (!isValidLanguage(language)) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid language code",
                    "validLanguages", "en, mr, hi",
                    "details", "The provided language code is not supported"
                ));
            }
            
            Map<String, String> translations = languageService.getAllTranslations(language);
            if (translations.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "Translations not found",
                    "details", "No translations available for the specified language",
                    "language", language
                ));
            }
            
            // Cache translations for 1 hour
            return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .body(translations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Failed to load translations",
                "details", e.getMessage(),
                "language", language
            ));
        }
    }

    @GetMapping("/translate/{key}/{language}")
    public ResponseEntity<?> getTranslation(
            @PathVariable String key,
            @PathVariable String language) {
        try {
            if (!isValidLanguage(language)) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid language code",
                    "validLanguages", "en, mr, hi"
                ));
            }

            String translation = languageService.getTranslation(key, language);
            return ResponseEntity.ok(Map.of("translation", translation));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to get translation",
                "message", e.getMessage()
            ));
        }
    }

    private boolean isValidLanguage(String language) {
        return language != null && (
            language.equals("en") || 
            language.equals("mr") || 
            language.equals("hi")
        );
    }
} 