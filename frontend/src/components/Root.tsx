import * as Device from "expo-device";
import * as Notifications from "expo-notifications";
import { StatusBar } from "expo-status-bar";
import React, { useEffect, useState } from "react";
import { Platform, StyleSheet, Text, View } from "react-native";
import { getExpoConfigExtra } from "@/config";

Notifications.setNotificationHandler({
  handleNotification: () =>
    Promise.resolve({
      shouldPlaySound: true,
      shouldSetBadge: false,
      shouldShowAlert: true,
    }),
});

export function Root() {
  const [expoPushToken, setExpoPushToken] = useState<string>("");
  const [notification, setNotification] =
    useState<Notifications.Notification | null>(null);

  console.log("OK");
  useEffect(() => {
    void getExpoPushToken().then((token) => {
      console.log(token);
      void (async (expoPushToken: string): Promise<void> => {
        const response = await fetch("http://10.0.0.6:3000/expo_push_tokens", {
          headers: {
            "Content-Type": "application/json",
          },
          method: "POST",
          body: JSON.stringify({ expo_push_token: expoPushToken }),
        });
        if (!response.ok) {
          throw new Error(`status = ${response.status}`);
        }
      })(token);
      setExpoPushToken(token);
    });

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
      <StatusBar style="auto" />
    </View>
  );
}

async function getExpoPushToken(): Promise<string> {
  const projectId = getExpoConfigExtra().eas.projectId;

  if (!Device.isDevice) {
    throw new Error("Must use physical device for Push Notifications");
  }
  console.log("Device.isDevice=true");

  if (Platform.OS === "android") {
    await Notifications.setNotificationChannelAsync("default", {
      name: "default",
      importance: Notifications.AndroidImportance.DEFAULT,
    });
  }
  console.log(`Platform.OS=${Platform.OS}`);

  const { status: existingStatus } = await Notifications.getPermissionsAsync();
  console.log(`getPermissionsAsync=${existingStatus}`);
  let finalStatus = existingStatus;
  if ((existingStatus as string) !== "granted") {
    const { status } = await Notifications.requestPermissionsAsync();
    finalStatus = status;
  }
  if ((finalStatus as string) !== "granted") {
    throw new Error("Failed to get push token for push notification!");
  }
  return (
    await Notifications.getExpoPushTokenAsync({
      projectId,
    })
  ).data;
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
});
