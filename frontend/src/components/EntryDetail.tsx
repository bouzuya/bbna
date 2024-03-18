import { StyleSheet, Text, View } from "react-native";
import { useRoute } from "@/hooks/useNavigation";

export function EntryDetail(): JSX.Element {
  const route = useRoute<"EntryDetail">();
  return (
    <View style={styles.container}>
      <Text>Entry Detail {route.params.date}</Text>
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
