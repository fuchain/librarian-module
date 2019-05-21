import Vue from "vue";
import App from "./App.vue";

// Vuesax Component Framework
import Vuesax from "vuesax";
import "material-icons/iconfont/material-icons.css"; // Material Icons
import "vuesax/dist/vuesax.css";
// Theme Configurations
import "../themeConfig.js";
// Globally Registered Components
import "./globalComponents.js";
// Styles: SCSS
import "./assets/scss/main.scss";
// Tailwind
import "@/assets/css/main.css";

import router from "./router";
import store from "./store/store";

// PrismJS
import "prismjs";
import "prismjs/themes/prism-tomorrow.css";

// Time Ago
import VueTimeago from "vue-timeago";

import http from "@http";
import auth from "@auth";

// Vuesax
Vue.use(Vuesax);

// Feather font icon
require("./assets/css/iconfont.css");

Vue.use({
    install(Vue) {
        Vue.prototype.$http = http;
    }
});
Vue.use({
    install(Vue) {
        Vue.prototype.$auth = auth;
    }
});

Vue.use(VueTimeago, {
    name: "Timeago", // Component name, `Timeago` by default
    locale: "en", // Default locale
    // We use `date-fns` under the hood
    // So you can use all locales from it
    locales: {
        "zh-CN": require("date-fns/locale/zh_cn"),
        ja: require("date-fns/locale/ja")
    }
});

Vue.config.productionTip = false;

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount("#app");
