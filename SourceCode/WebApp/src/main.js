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

// Form Wizard
import VueFormWizard from "vue-form-wizard";

import http from "@http";
import auth from "@auth";
import * as localStorage from "@localstorage";

import VueMoment from "vue-moment";
import moment from "moment";
import "moment/locale/vi";

// Vue tour
import VueTour from "vue-tour";
import "vue-tour/dist/vue-tour.css";
Vue.use(VueTour);

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

window.vue = new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
