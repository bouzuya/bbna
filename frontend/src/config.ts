import Constants from "expo-constants";

export type ExpoConfigExtra = {
  apiUrl: string;
  eas: {
    projectId: string;
  };
};

export function getExpoConfigExtra(): ExpoConfigExtra {
  const extra = Constants.expoConfig?.extra;
  if (extra === undefined) {
    throw new Error("extra is undefined");
  }

  // apiUrl
  const apiUrl: unknown = extra.apiUrl;
  if (apiUrl === undefined) {
    throw new Error("extra.apiUrl key not found");
  }
  if (typeof apiUrl !== "string") {
    throw new Error("extra.apiUrl is not string");
  }

  // eas
  const eas: unknown = extra.eas;
  if (eas === undefined) {
    throw new Error("extra.eas key not found");
  }
  if (typeof eas !== "object" || eas === null) {
    throw new Error("extra.eas is not object");
  }

  // eas.projectId
  if (!("projectId" in eas)) {
    throw new Error("extra.eas.projectId key not found");
  }
  const { projectId } = eas;
  if (typeof projectId !== "string") {
    throw new Error("extra.eas.projectId is not string");
  }

  return {
    apiUrl,
    eas: {
      projectId,
    },
  };
}
