import * as Notifications from "expo-notifications";
import { useEffect } from "react";
import { getExpoConfigExtra } from "@/config";
import { getExpoPushToken } from "@/notifications";

export function useRoot() {
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
    })();

    const subscription = Notifications.addNotificationReceivedListener(
      (notification) => {
        console.log(notification);
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
}
