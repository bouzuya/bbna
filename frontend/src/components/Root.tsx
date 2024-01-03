import Constants from "expo-constants";
import * as Device from "expo-device";
import * as Notifications from "expo-notifications";
import { StatusBar } from "expo-status-bar";
import React, { useEffect, useState } from "react";
import { Platform, StyleSheet, Text, View } from "react-native";

Notifications.setNotificationHandler({
  handleNotification: () =>
    Promise.resolve({
      shouldPlaySound: true,
      shouldSetBadge: false,
      shouldShowAlert: true,
    }),
});

function getEasProjectId(): string {
  const extra = Constants.expoConfig?.extra;
  if (extra === undefined) {
    throw new Error("extra is undefined");
  }
  const eas: unknown = extra.eas;
  if (eas === undefined) {
    throw new Error("extra.eas key not found");
  }
  if (typeof eas !== "object" || eas === null) {
    throw new Error("eas is not object");
  }
  if (!("projectId" in eas)) {
    throw new Error("eas.projectId key not found");
  }
  const { projectId } = eas;
  if (typeof projectId !== "string") {
    throw new Error("projectId is not string");
  }
  return projectId;
}

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
      projectId: getEasProjectId(),
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
