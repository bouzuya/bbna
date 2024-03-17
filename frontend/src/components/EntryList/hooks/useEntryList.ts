import type { Entry } from "@/types/Entry";

export function useEntryList(): {
  entryList: Entry[] | null;
  onClickEntry: (date: string) => void;
} {
  const entryList = new Array(100).fill(0).map((_, i) => ({
    date: new Date(new Date("2020-01-02").getTime() + 24 * 60 * 60 * 1000 * i)
      .toISOString()
      .substring(0, 10),
    minutes: 15,
    pubdate: "2020-01-02T03:04:05Z",
    tags: ["misc"],
    title: `タイトル${i + 1}`,
  }));
  const onClickEntry = (date: string) => {
    console.log(`onClickEntry(date = ${date})`);
  };
  return {
    entryList,
    onClickEntry,
  };
}
