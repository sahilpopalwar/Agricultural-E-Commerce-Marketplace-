package org.project.oopjava;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class PasswordController {
    private final PasswordResetService resetService;

    PasswordController(PasswordResetService resetService) {
        this.resetService = resetService;
    }

    @GetMapping({"/forget-password", "/forgot-password"})
    public String showForgotPasswordForm(Model model) {
        return "forget-password";
    }

    @PostMapping({"/forget-password", "/forgot-password"})
    public String processForgotPasswordForm(
            @RequestParam(required = false) String email, Model model) {
        try {
            String token = resetService.createToken(email);
            if (token != null) {
                resetService.deliver(email, token);
            }
            model.addAttribute("successMessage", "If the account exists, reset instructions will be sent.");
        } catch (PasswordResetUnavailableException ex) {
            model.addAttribute("errorMessage", "Password reset is temporarily unavailable.");
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", "Enter a valid account email.");
        }
        return "forget-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String token,
            @RequestParam("new-password") String newPassword,
            Model model) {
        if (!resetService.reset(token, newPassword)) {
            model.addAttribute("errorMessage", "The reset link is invalid, expired, or already used.");
        } else {
            model.addAttribute("successMessage", "Password updated. You can now log in.");
        }
        return "forget-password";
    }
}
