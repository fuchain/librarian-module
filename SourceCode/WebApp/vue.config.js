module.exports = {
  publicPath: "/",
  pluginOptions: {
    i18n: {
      locale: "en",
      fallbackLocale: "en",
      localeDir: "locales",
      enableInSFC: false
    }
  },
  configureWebpack: {
    devtool: "source-map",
    resolve: {
      extensions: [".vue", ".js", ".jsx", ".json"],
      alias: {
        "@core": require("path").resolve(__dirname, "src/core"),
        "@localstorage": require("path").resolve(
          __dirname,
          "src/core/local-storage"
        ),
        "@http": require("path").resolve(__dirname, "src/core/http"),
        "@auth": require("path").resolve(__dirname, "src/core/auth")
      }
    }
  }
};
