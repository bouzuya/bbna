import Constants from "expo-constants";

export type ExpoConfigExtra = {
  eas: {
    projectId: string;
  };
};

export function getExpoConfigExtra(): ExpoConfigExtra {
  const extra = Constants.expoConfig?.extra;
  if (extra === undefined) {
    throw new Error("extra is undefined");
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
    eas: {
      projectId,
    },
  };
}
