import navbarSearchAndPinList from "@/views/layouts/components/navbarSearchAndPinList";
import themeConfig, { colors } from "@/../themeConfig.js";

const state = {
  // Theme's state
  isSidebarActive: true,
  breakpoint: null,
  sidebarWidth: "default",
  reduceButton: themeConfig.sidebarCollapsed,
  bodyOverlay: false,
  sidebarItemsMin: false,
  theme: themeConfig.theme || "light",
  navbarSearchAndPinList: navbarSearchAndPinList,
  themePrimaryColor: colors.primary,
  starredPages: navbarSearchAndPinList.data.filter(
    page => page.highlightAction
  ),
  currentColor: null,

  // Profile
  email: "",
  fullname: "",
  phone: "",

  // Num of books
  numOfKeepingBooks: 0,
  numOfRequestingBooks: 0,
  numOfReturningBooks: 0,

  // Notification
  notifications: [],

  // Transaction Popup
  transactionPopupOpen: false,
  transactionPopupTx: null
};

export default state;
