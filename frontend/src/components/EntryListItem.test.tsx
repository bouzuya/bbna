import { render } from "@testing-library/react-native";
import { describe, expect, it } from "vitest";
import { EntryListItem } from "@/components/EntryListItem";
import type { Entry } from "@/types/Entry";

describe("EntryListItem", () => {
  it("should show date and title", () => {
    const entry = buildEntry();
    const { getByText } = render(<EntryListItem entry={entry} />);
    expect(getByText(entry.date)).toBeTruthy();
    expect(getByText(entry.title)).toBeTruthy();
  });

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
