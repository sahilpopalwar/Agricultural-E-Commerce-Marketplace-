import { useEffect, useRef } from "react";
import { Navigation } from "./Navigation";
import "./about-theme.css";

type TemplatePageProps = {
  body: string;
  assets: string[];
};

export function TemplatePage({ body, assets }: TemplatePageProps) {
  const containerRef = useRef<HTMLDivElement>(null);
  const path = window.location.pathname;
  const showNavigation = !path.startsWith("/login")
    && path !== "/clerk-login"
    && !path.startsWith("/error");
  const bodyWithoutHeader = body.replace(/<header\b[^>]*>[\s\S]*?<\/header>/i, "");

  useEffect(() => {
    const injected: HTMLElement[] = [];

    for (const asset of assets) {
      const [kind, value] = asset.split(":", 2);
      if (!value) continue;
      if (kind === "link" && /\/css\/nav(?:1)?\.css$/.test(value)) continue;

      if (kind === "link" && !document.head.querySelector(`link[href="${value}"]`)) {
        const link = document.createElement("link");
        link.rel = "stylesheet";
        link.href = value;
        link.dataset.reactAsset = "true";
        document.head.appendChild(link);
        injected.push(link);
      }

      if (kind === "script" && !document.querySelector(`script[src="${value}"]`)) {
        const script = document.createElement("script");
        script.src = value;
        script.dataset.reactAsset = "true";
        document.body.appendChild(script);
        injected.push(script);
      }
    }

    return () => {
      injected.forEach((asset) => asset.remove());
    };
  }, [assets]);

  return (
    <div className="tsx-page" ref={containerRef}>
      {showNavigation && <Navigation />}
      <div dangerouslySetInnerHTML={{ __html: bodyWithoutHeader }} />
    </div>
  );
}
