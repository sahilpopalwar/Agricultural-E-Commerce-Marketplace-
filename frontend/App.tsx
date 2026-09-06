import { useEffect } from "react";
import { routeComponents } from "./routes";

/**
 * The Thymeleaf documents remain the server-rendered source of truth.  This
 * app is deliberately additive: it identifies the current Spring route and
 * provides a React mount point without replacing the existing DOM, styles,
 * forms, CSRF fields, or asset URLs.
 */
const routes: Record<string, string> = {
  "/": "project",
  "/login": "login",
  "/clerk-login": "clerk-login",
  "/farmer-register": "farmerreg",
  "/services": "services",
  "/about": "about",
  "/contact": "contact",
  "/buyNow": "buyNow",
  "/user": "user",
  "/forget-password": "forget-password",
  "/add-to-cart": "add_to_cart",
  "/userabout": "userabout",
  "/usercontact": "usercontact",
  "/userservices": "userservices",
  "/order": "order",
  "/profile": "profile",
  "/wishlist": "wishlist",
  "/settings": "settings",
};

export function App() {
  const path = window.location.pathname.replace(/\/$/, "") || "/";
  const template = routes[path] ?? "error";
  const Route = routeComponents[path] ?? routeComponents["/error/404"];

  useEffect(() => {
    document.documentElement.dataset.reactRoute = path;
    document.body.dataset.reactTemplate = template;
    return () => {
      delete document.documentElement.dataset.reactRoute;
      delete document.body.dataset.reactTemplate;
    };
  }, [path, template]);

  return <Route />;
}
