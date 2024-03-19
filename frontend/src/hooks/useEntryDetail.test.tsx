import { renderHook } from "@testing-library/react-native";
import * as Swr from "swr";
import type { MockInstance } from "vitest";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { useEntryDetail } from "@/hooks/useEntryDetail";
import * as UseNavigation from "@/hooks/useNavigation";

vi.mock("swr", () => ({
  default: vi.fn(),
}));

describe("useEntryDetail", () => {
  let mockSetOptions: MockInstance;
  let mockUseNavigation: MockInstance;
  let mockUseRoute: MockInstance;
  let mockUseSWR: MockInstance;

  beforeEach(() => {
    mockSetOptions = vi.fn();
    mockUseNavigation = vi
      .spyOn(UseNavigation, "useNavigation")
      .mockReturnValue({
        setOptions: mockSetOptions,
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
      } as any as ReturnType<typeof UseNavigation.useNavigation>);
    mockUseRoute = vi.spyOn(UseNavigation, "useRoute").mockReturnValue({
      key: "",
      name: "EntryDetail",
      params: { date: "2020-01-02" },
    });
    mockUseSWR = vi.spyOn(Swr, "default").mockReturnValue({
      data: null,
      error: undefined,
      isLoading: true,
      isValidating: false,
      mutate: () => Promise.reject(new Error("mutate is not implemented")),
    });
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it("is a function", () => {
    expect(typeof useEntryDetail).toBe("function");
  });

  it("calls useSWR with https://blog.bouzuya.net/${date}.json", () => {
    const { result } = renderHook(() => useEntryDetail());
    expect(result.current.date).toBe("2020-01-02");
    expect(result.current.entryDetail).toBe(null);
    expect(mockUseNavigation).toHaveBeenCalledTimes(1);
    expect(mockUseRoute).toHaveBeenCalledTimes(1);
    expect(mockUseSWR).toHaveBeenCalledWith(
      "https://blog.bouzuya.net/2020/01/02.json",
      expect.any(Function),
    );
  });

  it("calls useNavigation setOptions with date", () => {
    const { result } = renderHook(() => useEntryDetail());
    expect(result.current.date).toBe("2020-01-02");
    expect(result.current.entryDetail).toBe(null);
    expect(mockUseNavigation).toHaveBeenCalledTimes(1);
    expect(mockSetOptions).toHaveBeenCalledWith({ title: "2020-01-02" });
  });
});
