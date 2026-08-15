package org.project.oopjava;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String home() {
        return "project";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
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

    @GetMapping("/fotget-password")
    public String fotgetPassword() {
        return "fotget-password";
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

@GetMapping("/Wishlist")
public String wishlist() {
    return "Wishlist";
}

@GetMapping("/settings")
public String settings() {
    return "settings";
}
   
}