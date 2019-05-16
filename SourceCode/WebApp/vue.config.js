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
        "@utils": require("path").resolve(__dirname, "src/utils"),
        "@localstorage": require("path").resolve(__dirname, "src/utils/local-storage"),
        "@http": require("path").resolve(__dirname, "src/utils/http"),
        "@auth": require("path").resolve(__dirname, "src/utils/auth"),
        "@mock": require("path").resolve(__dirname, "src/mock")
      }
    }
  }
};
