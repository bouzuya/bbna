import { FlatList, StyleSheet, TouchableOpacity, View } from "react-native";
import { useEntryList } from "@/components/EntryList/hooks/useEntryList";
import { EntryListItem } from "@/components/EntryListItem";

export function EntryList(): JSX.Element {
  const { entryList, onClickEntry } = useEntryList();
  return (
    <View style={styles.container}>
      <FlatList
        data={entryList}
        renderItem={({ item }) => (
          <View key={item.date} style={styles.itemContainer}>
            <TouchableOpacity
              onPress={() => {
                onClickEntry(item.date);
              }}
            >
              <EntryListItem entry={item} />
            </TouchableOpacity>
          </View>
        )}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 0,
    height: "100%",
    width: "100%",
  },
  itemContainer: {
    height: 80,
    width: "100%",
  },
});
