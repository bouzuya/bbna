import { ActivityIndicator, ScrollView, StyleSheet, View } from "react-native";
import Markdown from "react-native-markdown-display";
import { useEntryDetail } from "@/hooks/useEntryDetail";

export function EntryDetail(): JSX.Element {
  const { entryDetail } = useEntryDetail();

  return (
    <View style={styles.container}>
      {entryDetail === null ? (
        <View
          style={{
            alignItems: "center",
            // header height
            marginTop: -64,
            justifyContent: "center",
            flex: 1,
          }}
        >
          <ActivityIndicator size="large" />
        </View>
      ) : (
        <ScrollView>
          <Markdown
            style={{
              body: { fontSize: 16, padding: 16, paddingVertical: 32 },
              heading1: { fontSize: 28, fontWeight: "bold", paddingBottom: 24 },
              heading2: { fontSize: 24, fontWeight: "bold", paddingBottom: 24 },
              heading3: { fontSize: 20, fontWeight: "bold", paddingBottom: 24 },
              heading4: { fontSize: 16, fontWeight: "bold", paddingBottom: 16 },
              heading5: { fontSize: 16, fontWeight: "bold", paddingBottom: 16 },
              heading6: { fontSize: 16, fontWeight: "bold", paddingBottom: 16 },
              hr: {},
              strong: {},
              em: {},
              s: {},
              blockquote: {},
              bullet_list: {},
              ordered_list: {},
              list_item: {},
              code_inline: {},
              code_block: {},
              fence: {},
              table: {},
              thead: {},
              tbody: {},
              th: {},
              tr: {},
              td: {},
              link: {},
              blocklink: {},
              image: {},
              text: {},
              textgroup: {},
              paragraph: {},
              hardbreak: {},
              softbreak: {},
              pre: {},
              inline: {},
              span: {},
            }}
          >
            {entryDetail.data}
          </Markdown>
        </ScrollView>
      )}
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
