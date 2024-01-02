module.exports = function (api) {
  api.cache(true);
  return {
    presets: ["babel-preset-expo"],
    plugins: [
      [
        "module-resolver",
        {
          alias: {
            "@": "./src",
          },
          extensions: [".android.js", ".ios.js", ".json", ".js", ".ts", ".tsx"],
          root: ["."],
        },
      ],
    ],
  };
};
