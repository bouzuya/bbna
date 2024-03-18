import { useEffect } from "react";
import useSWR from "swr";
import { useNavigation, useRoute } from "@/hooks/useNavigation";
import type { EntryDetail } from "@/types/EntryDetail";

export function useEntryDetail(): {
  date: string;
  entryDetail: EntryDetail | null;
} {
  const {
    params: { date },
  } = useRoute<"EntryDetail">();
  const navigation = useNavigation();

  const { data: entryDetail } = useSWR<EntryDetail>(
    `https://blog.bouzuya.net/${date.replaceAll("-", "/")}.json`,
    (key: string) => fetch(key).then((res): Promise<EntryDetail> => res.json()),
  );

  useEffect(() => {
    navigation.setOptions({ title: `${date} ${entryDetail?.title ?? ""}` });
  }, [date, entryDetail, navigation]);

  return { date, entryDetail: entryDetail ?? null };
}
