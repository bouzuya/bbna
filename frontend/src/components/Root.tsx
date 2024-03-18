import * as Notifications from "expo-notifications";
import React from "react";
import { EntryDetail } from "@/components/EntryDetail";
import { EntryList } from "@/components/EntryList";
import { NavigationContainer, Stack } from "@/hooks/useNavigation";
import { useRoot } from "@/hooks/useRoot";

Notifications.setNotificationHandler({
  handleNotification: () =>
    Promise.resolve({
      shouldPlaySound: true,
      shouldSetBadge: false,
      shouldShowAlert: true,
    }),
});

export function Root() {
  useRoot();
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="EntryList">
        <Stack.Screen name="EntryDetail" component={EntryDetail} />
        <Stack.Screen
          name="EntryList"
          component={EntryList}
          options={{ title: "blog.bouzuya.net" }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
