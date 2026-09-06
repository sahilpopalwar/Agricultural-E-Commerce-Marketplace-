import { useEffect, useState } from "react";

const links = [
  ["/", "Home"],
  ["/about", "About"],
  ["/services", "Services"],
  ["/contact", "Contact"],
] as const;

export function Navigation() {
  const [scrolled, setScrolled] = useState(false);
  const path = window.location.pathname;

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 50);
    window.addEventListener("scroll", onScroll);
    onScroll();
    return () => window.removeEventListener("scroll", onScroll);
  }, []);

  return (
    <header className={scrolled ? "tsx-navigation scrolled" : "tsx-navigation"} id="mainHeader">
      <a className="logo" href="/">
        <i className="fas fa-leaf" aria-hidden="true" /> Fresh Market
      </a>
      <nav aria-label="Main navigation">
        <ul>
          {links.map(([href, label]) => (
            <li key={href}>
              <a className={path === href ? "active" : ""} href={href}>{label}</a>
            </li>
          ))}
          <li>
            <a href="/login" className="tsx-profile-link" aria-label="Sign in">
              <img src="/profile.jpg" alt="Profile" id="profile-img" />
            </a>
          </li>
        </ul>
      </nav>
    </header>
  );
}
