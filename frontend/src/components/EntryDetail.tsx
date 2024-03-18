import { StyleSheet, Text, View } from "react-native";
import { useEntryDetail } from "@/hooks/useEntryDetail";

export function EntryDetail(): JSX.Element {
  const { date } = useEntryDetail();

  return (
    <View style={styles.container}>
      <Text>Entry Detail {date}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 0,
    height: "100%",
    width: "100%",
  },
});
