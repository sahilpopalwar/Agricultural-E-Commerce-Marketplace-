import type { ComponentType } from "react";
import { ProjectPage } from "./pages/ProjectPage";
import { LoginPage } from "./pages/LoginPage";
import { ClerkLoginPage } from "./pages/ClerkLoginPage";
import { ServicesPage } from "./pages/ServicesPage";
import { AboutPage } from "./pages/AboutPage";
import { ContactPage } from "./pages/ContactPage";
import { BuyNowPage } from "./pages/BuyNowPage";
import { UserPage } from "./pages/UserPage";
import { ForgetPasswordPage } from "./pages/ForgetPasswordPage";
import { AddToCartPage } from "./pages/AddToCartPage";
import { UserAboutPage } from "./pages/UserAboutPage";
import { UserContactPage } from "./pages/UserContactPage";
import { UserServicesPage } from "./pages/UserServicesPage";
import { OrderPage } from "./pages/OrderPage";
import { ProfilePage } from "./pages/ProfilePage";
import { WishlistPage } from "./pages/WishlistPage";
import { SettingsPage } from "./pages/SettingsPage";
import { Error403Page } from "./pages/Error403Page";
import { Error404Page } from "./pages/Error404Page";
import { Error500Page } from "./pages/Error500Page";

export const routeComponents: Record<string, ComponentType> = {
  "/": ProjectPage,
  "/login": LoginPage,
  "/clerk-login": ClerkLoginPage,
  "/farmer-register": ProjectPage,
  "/services": ServicesPage,
  "/about": AboutPage,
  "/contact": ContactPage,
  "/buyNow": BuyNowPage,
  "/user": UserPage,
  "/forget-password": ForgetPasswordPage,
  "/add-to-cart": AddToCartPage,
  "/userabout": UserAboutPage,
  "/usercontact": UserContactPage,
  "/userservices": UserServicesPage,
  "/order": OrderPage,
  "/profile": ProfilePage,
  "/wishlist": WishlistPage,
  "/settings": SettingsPage,
  "/error/403": Error403Page,
  "/error/404": Error404Page,
  "/error/500": Error500Page,
};
