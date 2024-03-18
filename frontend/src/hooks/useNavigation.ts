import * as ReactNavigation from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import type { NativeStackNavigationProp } from "@react-navigation/native-stack";

type ParamList = {
  EntryDetail: {
    date: string;
  };
  EntryList: undefined;
};

export const NavigationContainer = ReactNavigation.NavigationContainer;

export const Stack = createNativeStackNavigator<ParamList>();

export const useNavigation: () => NativeStackNavigationProp<ParamList> =
  ReactNavigation.useNavigation<NativeStackNavigationProp<ParamList>>;

export function useRoute<
  T extends keyof ParamList,
>(): ReactNavigation.RouteProp<ParamList, T> {
  return ReactNavigation.useRoute<ReactNavigation.RouteProp<ParamList, T>>();
}
