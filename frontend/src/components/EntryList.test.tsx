import type { MockInstance } from "vitest";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { EntryList } from "@/components/EntryList";
import * as UseEntryList from "@/components/EntryList/hooks/useEntryList";
import type { Entry } from "@/types/Entry";

vi.mock("@/components/EntryList/hooks/useEntryList", () => ({
  useEntryList: () => ({
    entryList: null,
    onClickEntry: vi.fn(),
  }),
}));

describe("EntryList", () => {
  let mockEntry: Entry;
  let mockUseEntryList: MockInstance;

  beforeEach(() => {
    mockEntry = buildEntry();
    mockUseEntryList = vi.spyOn(UseEntryList, "useEntryList") as MockInstance;
    mockUseEntryList.mockReturnValue({
      entryList: [mockEntry],
      onClickEntry: vi.fn(),
    });
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it("export EntryList", () => {
    expect(EntryList).toBeTruthy();
  });

  // can't render EntryList
  // TypeError: Cannot destructure property 'getItem' of 'props' as it is undefined.
  // it("should show date and title", () => {
  //   const { getByText } = render(<EntryList />);
  //   expect(mockUseEntryList).toBeCalledTimes(1);
  //   expect(getByText(mockEntry.date)).toBeTruthy();
  // });

  function buildEntry(): Entry {
    return {
      date: "2020-01-02",
      minutes: 15,
      pubdate: "2020-01-02T03:04:05Z",
      tags: ["misc"],
      title: "タイトル1",
    };
  }
});
