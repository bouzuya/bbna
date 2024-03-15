import { describe, expect, it, vi } from "vitest";
import { getExpoConfigExtra } from "@/config";

vi.mock("expo-constants", () => ({
  default: {
    expoConfig: {
      extra: {
        apiUrl: "https://example.com/backend",
        eas: {
          projectId: "projectId1",
        },
      },
    },
  },
}));

describe("getExpoConfigExtra", () => {
  it("should return apiUrl", () => {
    expect(getExpoConfigExtra().apiUrl).toEqual("https://example.com/backend");
  });

  it("should return eas.projectId", () => {
    expect(getExpoConfigExtra().eas.projectId).toEqual("projectId1");
  });
});
