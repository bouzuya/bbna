import { describe, expect, it, vi } from "vitest";
import { getExpoConfigExtra } from "@/config";

vi.mock("expo-constants", () => ({
  default: {
    expoConfig: {
      extra: {
        eas: {
          projectId: "projectId1",
        },
      },
    },
  },
}));

describe("getExpoConfigExtra", () => {
  it("should return eas.projectId", () => {
    expect(getExpoConfigExtra()).toEqual({
      eas: {
        projectId: "projectId1",
      },
    });
  });
});
