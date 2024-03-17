import path from "node:path";
import react from "@vitejs/plugin-react";
import { defineConfig } from "vitest/config";
import viestReactNative from "vitest-react-native";

// eslint-disable-next-line import/no-default-export
export default defineConfig({
  plugins: [viestReactNative(), react()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
});
