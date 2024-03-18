import * as Notifications from "expo-notifications";
import React from "react";
import { EntryDetail } from "@/components/EntryDetail";
import { EntryList } from "@/components/EntryList";
import {
  NavigationContainer,
  Stack,
} from "@/components/Root/hooks/useNavigation";
import { useRoot } from "@/components/Root/hooks/useRoot";

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
        <Stack.Screen name="EntryList" component={EntryList} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
