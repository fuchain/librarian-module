// MAIN COLORS - VUESAX THEME COLORS
import Vue from "vue";
import Vuesax from "vuesax";

export let colors = {
  primary: "#3C8CBC",
  success: "#28C76F",
  danger: "#EA5455",
  warning: "#FF9F43",
  dark: "#1E1E1E"
};
Vue.use(Vuesax, { theme: { colors } });

// CONFIGS
const themeConfig = {
  theme: "light", // options[String]: 'light'(default), 'dark', 'semi-dark'
  sidebarCollapsed: false, // options[Boolean]: true, false(default)
  navbarColor: "#fff", // options[String]: HEX color / rgb / rgba / Valid HTML Color name - (default: #fff)
  navbarType: "floating", // options[String]: floating(default) / static / sticky / hidden
  footerType: "static", // options[String]: static(default) / sticky / hidden
  routerTransition: "zoom-fade", // options[String]: zoom-fade / slide-fade / fade-bottom / fade / zoom-out / none(default)
  disableCustomizer: true // options[Boolean]: true, false(default)
};

export default themeConfig;
