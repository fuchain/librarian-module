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

import vueDebounce from "vue-debounce";

// Form Wizard
import VueFormWizard from "vue-form-wizard";

import http from "@http";
import auth from "@auth";
import * as localStorage from "@localstorage";

import VueMoment from "vue-moment";
import moment from "moment";
import "moment/locale/vi";

import VueFriendlyIframe from "vue-friendly-iframe";

import isMobile from "./isMobile";

// Vue tour
import VueTour from "vue-tour";
import "vue-tour/dist/vue-tour.css";

// Sentry
import * as Sentry from "@sentry/browser";
import * as Integrations from "@sentry/integrations";

import VueProgressBar from "vue-progressbar";
import "./registerServiceWorker";

Vue.use(VueProgressBar, {
  color: "#7367F0",
  failedColor: "#EA5455",
  height: "10px",
  thickness: "8px",
  autoFinish: false,
  transition: {
    speed: "0.1s",
    opacity: "1s",
    termination: 300
  }
});

Vue.use(vueDebounce);

!window.webpackHotUpdate &&
  Sentry.init({
    dsn: "https://900117affb2c4742a2864410b864462f@sentry.io/1521086",
    integrations: [new Integrations.Vue({ Vue, attachProps: true })]
  });

Vue.use(VueTour);

Vue.component("vue-friendly-iframe", VueFriendlyIframe);

// Form Wizard
Vue.use(VueFormWizard);

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
Vue.use({
  install(Vue) {
    Vue.prototype.$localStorage = localStorage;
  }
});

Vue.use(VueTimeago, {
  name: "Timeago", // Component name, `Timeago` by default
  locale: "vi", // Default locale
  // We use `date-fns` under the hood
  // So you can use all locales from it
  locales: {
    vi: require("date-fns/locale/vi")
  }
});

Vue.use(VueMoment, {
  moment
});

Vue.config.productionTip = false;

window.isMobile = isMobile;

window.vue = new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
