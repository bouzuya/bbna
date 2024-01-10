import * as Device from "expo-device";
import * as Notifications from "expo-notifications";
import { Platform } from "react-native";
import { getExpoConfigExtra } from "@/config";

export async function getExpoPushToken(): Promise<string | null> {
  const projectId = getExpoConfigExtra().eas.projectId;

  if (!Device.isDevice) return null;

  if (Platform.OS === "android") {
    await Notifications.setNotificationChannelAsync("default", {
      name: "default",
      importance: Notifications.AndroidImportance.DEFAULT,
    });
  }

  const granted = await (async (): Promise<boolean> => {
    const { granted: alreadyGranted } =
      await Notifications.getPermissionsAsync();
    if (alreadyGranted) return true;
    const { granted } = await Notifications.requestPermissionsAsync();
    return granted;
  })();

  if (!granted) return null;

  const expoPushToken = await Notifications.getExpoPushTokenAsync({
    projectId,
  });

  return expoPushToken.data;
}
