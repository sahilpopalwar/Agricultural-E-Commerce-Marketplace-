import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { resolve } from "node:path";

export default defineConfig({
  root: resolve(__dirname, "frontend"),
  plugins: [react()],
  base: "/frontend/",
  build: {
    outDir: resolve(__dirname, "src/main/resources/static/frontend"),
    emptyOutDir: true,
  },
});
