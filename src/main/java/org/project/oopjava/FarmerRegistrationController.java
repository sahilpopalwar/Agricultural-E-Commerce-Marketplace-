package org.project.oopjava;

import org.project.oopjava.util.SecurityUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FarmerRegistrationController {
    private final JdbcTemplate jdbcTemplate;

    public FarmerRegistrationController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/farmer-register")
    public String register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam("confirm-password") String confirmPassword,
            @RequestParam("farm-name") String farmName,
            @RequestParam("farm-address") String farmAddress,
            Model model) {
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
                || name.isBlank() || phone.isBlank()
                || farmName.isBlank() || farmAddress.isBlank()
                || !password.equals(confirmPassword)
                || !SecurityUtils.isValidPassword(password)) {
            model.addAttribute("errorMessage", "Enter valid registration details and a strong password.");
            return "farmerreg";
        }
        try {
            String username = email.substring(0, email.indexOf('@'));
            if (username.length() > 50) {
                username = username.substring(0, 50);
            }
            jdbcTemplate.update("""
                    INSERT INTO users (username, phone, email, password_hash, role, farm_name, farm_address)
                    VALUES (?, ?, ?, ?, 'FARMER', ?, ?)
                    """, username, phone.trim(), email.trim().toLowerCase(),
                    SecurityUtils.encodePassword(password), farmName.trim(), farmAddress.trim());
            model.addAttribute("successMessage", "Registration successful. You can now log in.");
            return "login";
        } catch (DuplicateKeyException ex) {
            model.addAttribute("errorMessage", "An account with that email, phone, or username already exists.");
            return "farmerreg";
        }
    }
}
