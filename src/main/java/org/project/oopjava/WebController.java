package org.project.oopjava;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.util.ArrayList;

@Controller
public class WebController {
    @GetMapping("/")
    public String home() {
        return "project";
    }

    @GetMapping("/project")
    public String project() {
        return "project";
    }

    @GetMapping("/login")
    public String login(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String error,
            org.springframework.ui.Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "login";
    }

    @GetMapping("/clerk-login")
    public String clerkLogin(
            @Value("${app.clerk.publishable-key:}") String publishableKey,
            Model model) {
        model.addAttribute("clerkPublishableKey", publishableKey);
        return "clerk-login";
    }

    @GetMapping("/farmer-register")
    public String farmerRegister() {
        return "farmerreg";
    }

    // Main pages
    @GetMapping("/services")
    public String services() {
        return "services";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    // Shopping routes
    @GetMapping("/buyNow")
    public String buyNow() {
        return "buyNow";
    }

    // Shopping routes
    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/add-to-cart")
    public String addToCart() {
        return "add_to_cart";
    }

    @GetMapping("/userabout")
    public String userabout() {
        return "userabout";
    }
    
    @GetMapping("/usercontact")
    public String usercontact() {
        return "usercontact";
    }
    @GetMapping("/userservices")
    public String userservices() {
        return "userservices";
    }



@GetMapping("/order")
public String order() {
    return "order";
}

@GetMapping("/profile")
public String profile() {
    return "profile";
}

@GetMapping("/wishlist")
public String wishlist(Model model) {
    // Initialize empty wishlist items list
    model.addAttribute("wishlistItems", new ArrayList<>());
    return "wishlist";
}

@GetMapping("/settings")
public String settings() {
    return "settings";
}
   
}