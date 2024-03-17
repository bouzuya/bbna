import useSWR from "swr";
import type { Entry } from "@/types/Entry";

export function useEntryList(): {
  entryList: Entry[] | null;
  onClickEntry: (date: string) => void;
} {
  const { data: entryList } = useSWR<Entry[]>(
    "https://blog.bouzuya.net/posts.json",
    (key: string) =>
      fetch(key)
        .then((res): Promise<Entry[]> => res.json())
        .then((data) =>
          data.sort(({ date: a }, { date: b }) =>
            a === b ? 0 : a < b ? 1 : -1,
          ),
        ),
  );
  const onClickEntry = (date: string) => {
    console.log(`onClickEntry(date = ${date})`);
  };
  return {
    entryList: entryList ?? null,
    onClickEntry,
  };
}
