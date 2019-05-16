import navbarSearchAndPinList from "@/layouts/components/navbarSearchAndPinList";
import themeConfig, { colors } from "@/../themeConfig.js";

const state = {
  isSidebarActive: true,
  breakpoint: null,
  sidebarWidth: "default",
  reduceButton: themeConfig.sidebarCollapsed,
  bodyOverlay: false,
  sidebarItemsMin: false,
  theme: themeConfig.theme || "light",
  navbarSearchAndPinList: navbarSearchAndPinList,
  AppActiveUser: {
    id: 0,
    name: "Ido Bukin",
    about:
      "Dessert chocolate cake lemon drops jujubes. Biscuit cupcake ice cream bear claw brownie brownie marshmallow.",
    img: "avatar-s-11.png",
    status: "online"
  },

  themePrimaryColor: colors.primary,

  starredPages: navbarSearchAndPinList.data.filter(
    page => page.highlightAction
  ),

  currentColor: null
};

export default state;
