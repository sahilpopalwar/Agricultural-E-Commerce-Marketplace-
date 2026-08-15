package org.project.oopjava;

import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class PasswordController {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("passwordRecoveryForm", new PasswordRecoveryForm());
        return "forgot-password"; // Ensure this matches your HTML template name
    }

    @PostMapping("/forgot-password")
    public String processForgotPasswordForm(@RequestParam(required = false) String email, Model model) {
        if (email == null || email.isEmpty()) {
            model.addAttribute("errorMessage", "Email address cannot be empty.");
        } else if (!Pattern.matches(EMAIL_REGEX, email)) {
            model.addAttribute("errorMessage", "Please provide a valid email address.");
        } else {
            // Simulate sending a password reset link
            model.addAttribute("successMessage", "Password reset link has been sent to your email.");
        }

        return "forgot-password"; // Ensure this matches your HTML template name
    }
}

// Consider moving this class to a separate file for better organization
class PasswordRecoveryForm {

    private String email;

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
