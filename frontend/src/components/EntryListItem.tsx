import { StyleSheet, Text, View } from "react-native";
import type { Entry } from "@/types/Entry";

type Props = {
  entry: Entry;
};

export function EntryListItem({ entry: { date, title } }: Props): JSX.Element {
  return (
    <View style={styles.container}>
      <Text style={styles.dateText}>{date}</Text>
      <Text numberOfLines={1} style={styles.titleText}>
        {title}
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 0,
    flexDirection: "column",
    height: "100%",
    justifyContent: "center",
    padding: 16,
    width: "100%",
  },
  dateText: {
    fontSize: 14,
  },
  titleText: {
    fontSize: 16,
  },
});
