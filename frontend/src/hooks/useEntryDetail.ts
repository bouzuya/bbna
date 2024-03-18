import useSWR from "swr";
import { useRoute } from "@/hooks/useNavigation";
import type { EntryDetail } from "@/types/EntryDetail";

export function useEntryDetail(): {
  date: string;
  entryDetail: EntryDetail | null;
} {
  const {
    params: { date },
  } = useRoute<"EntryDetail">();

  const { data: entryDetail } = useSWR<EntryDetail>(
    `https://blog.bouzuya.net/${date.replaceAll("-", "/")}.json`,
    (key: string) => fetch(key).then((res): Promise<EntryDetail> => res.json()),
  );

  return { date, entryDetail: entryDetail ?? null };
}
