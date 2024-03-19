import { MaterialCommunityIcons } from "@expo/vector-icons";
import { useEffect } from "react";
import { Share, TouchableOpacity } from "react-native";
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
    navigation.setOptions({
      title: date,
      headerRight: () => (
        <TouchableOpacity
          onPress={() => {
            void Share.share({
              message: `https://blog.bouzuya.net/${date.replaceAll("-", "/")}/`,
            });
          }}
        >
          <MaterialCommunityIcons
            color="black"
            name="share-variant-outline"
            size={24}
            style={{ marginRight: 4 }}
          />
        </TouchableOpacity>
      ),
    });
  }, [date, entryDetail, navigation]);

  return { date, entryDetail: entryDetail ?? null };
}
