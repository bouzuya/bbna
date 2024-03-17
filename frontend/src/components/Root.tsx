import { NavigationContainer, useNavigation } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import type { NativeStackNavigationProp } from "@react-navigation/native-stack";
import * as Notifications from "expo-notifications";
import { StatusBar } from "expo-status-bar";
import React, { useEffect, useState } from "react";
import { StyleSheet, Text, View } from "react-native";
import { EntryList } from "@/components/EntryList";
import { getExpoConfigExtra } from "@/config";
import { getExpoPushToken } from "@/notifications";

Notifications.setNotificationHandler({
  handleNotification: () =>
    Promise.resolve({
      shouldPlaySound: true,
      shouldSetBadge: false,
      shouldShowAlert: true,
    }),
});

type ParamList = {
  EntryList: undefined;
  Home: undefined;
};

function Home(): JSX.Element {
  const [expoPushToken, setExpoPushToken] = useState<string>("");
  const [notification, setNotification] =
    useState<Notifications.Notification | null>(null);
  const navigation = useNavigation<NativeStackNavigationProp<ParamList>>();

  useEffect(() => {
    void (async () => {
      const token = await getExpoPushToken();
      if (token === null) return;
      const apiUrl = getExpoConfigExtra().apiUrl;

      await (async (expoPushToken: string): Promise<void> => {
        const response = await fetch(`${apiUrl}/expo_push_tokens`, {
          body: JSON.stringify({ expo_push_token: expoPushToken }),
          headers: {
            "Content-Type": "application/json",
          },
          method: "POST",
        });
        if (!response.ok) {
          throw new Error(`status = ${response.status}`);
        }
      })(token);

      setExpoPushToken(token);
    })();

    const subscription = Notifications.addNotificationReceivedListener(
      (notification) => {
        console.log(notification);
        setNotification(notification);
      },
    );

    const subscription2 = Notifications.addNotificationResponseReceivedListener(
      (response) => {
        console.log(response);
      },
    );

    return () => {
      console.log("unsubscribe");
      Notifications.removeNotificationSubscription(subscription);
      Notifications.removeNotificationSubscription(subscription2);
    };
  }, []);

  return (
    <View style={styles.container}>
      <Text>Your expo push token: {expoPushToken}</Text>
      <View style={{ alignItems: "center", justifyContent: "center" }}>
        <Text>Title: {notification?.request.content.title} </Text>
        <Text>Body: {notification?.request.content.body}</Text>
        <Text>
          Data:{" "}
          {notification && JSON.stringify(notification.request.content.data)}
        </Text>
      </View>
      <View
        style={{
          alignItems: "center",
          flex: 1,
          justifyContent: "center",
          width: "100%",
        }}
      >
        <Text
          onPress={() => {
            navigation.push("EntryList");
          }}
        >
          EntryList
        </Text>
      </View>
      <StatusBar style="auto" />
    </View>
  );
}

const Stack = createNativeStackNavigator<ParamList>();

export function Root() {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Home">
        <Stack.Screen name="EntryList" component={EntryList} />
        <Stack.Screen name="Home" component={Home} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
});
