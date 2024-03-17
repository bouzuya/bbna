import * as ReactNavigation from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import type { NativeStackNavigationProp } from "@react-navigation/native-stack";

type ParamList = {
  EntryDetail: {
    date: string;
  };
  EntryList: undefined;
  Home: undefined;
};

export const useNavigation = ReactNavigation.useNavigation<
  NativeStackNavigationProp<ParamList>
>;

export const NavigationContainer = ReactNavigation.NavigationContainer;

export const Stack = createNativeStackNavigator<ParamList>();
