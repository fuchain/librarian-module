import moment from "moment";

const mutations = {
  // ////////////////////////////////////////////
  // SIDEBAR & UI UX
  // ////////////////////////////////////////////

  UPDATE_SIDEBAR_WIDTH(state, width) {
    state.sidebarWidth = width;
  },
  UPDATE_SIDEBAR_ITEMS_MIN(state, val) {
    state.sidebarItemsMin = val;
  },
  TOGGLE_REDUCE_BUTTON(state, val) {
    state.reduceButton = val;
  },
  TOGGLE_CONTENT_OVERLAY(state, val) {
    state.bodyOverlay = val;
  },
  TOGGLE_IS_SIDEBAR_ACTIVE(state, value) {
    state.isSidebarActive = value;
  },
  UPDATE_THEME(state, val) {
    state.theme = val;
  },
  UPDATE_WINDOW_BREAKPOINT(state, val) {
    state.breakpoint = val;
  },
  UPDATE_PRIMARY_COLOR(state, val) {
    state.themePrimaryColor = val;
  },
  UPDATE_STATUS_CHAT(state, value) {
    state.AppActiveUser.status = value;
  },

  // ////////////////////////////////////////////
  // COMPONENT
  // ////////////////////////////////////////////

  // VxAutoSuggest
  UPDATE_STARRED_PAGE(state, payload) {
    // find item index in search list state
    const index = state.navbarSearchAndPinList.data.findIndex(
      item => item.index === payload.index
    );
    // update the main list
    state.navbarSearchAndPinList.data[index].highlightAction = payload.val;

    // if val is true add it to starred else remove
    if (payload.val) {
      state.starredPages.push(state.navbarSearchAndPinList.data[index]);
    } else {
      // find item index from starred pages
      const index = state.starredPages.findIndex(
        item => item.index === payload.index
      );
      // remove item using index
      state.starredPages.splice(index, 1);
    }
  },

  // The Navbar
  ARRANGE_STARRED_PAGES_LIMITED(state, list) {
    const starredPagesMore = state.starredPages.slice(10);
    state.starredPages = list.concat(starredPagesMore);
  },
  ARRANGE_STARRED_PAGES_MORE(state, list) {
    let downToUp = false;
    let lastItemInStarredLimited = state.starredPages[10];
    const starredPagesLimited = state.starredPages.slice(0, 10);
    state.starredPages = starredPagesLimited.concat(list);

    state.starredPages.slice(0, 10).map(i => {
      if (list.indexOf(i) > -1) downToUp = true;
    });
    if (!downToUp) {
      state.starredPages.splice(10, 0, lastItemInStarredLimited);
    }
  },

  PROFILE_UPDATED(state, profile) {
    state.email = profile.email;
    state.fullname = profile.fullname;
    state.phone = profile.phone;
  },
  PROFILE_UPDATED_SUCCESS(state, profile) {
    state.fullname = profile.fullname;
    state.phone = profile.phone;
  },
  UPDATE_PROFILE_EMAIL(state, email) {
    state.email = email;
  },
  UPDATE_PROFILE_FULLNAME(state, fullname) {
    state.fullname = fullname;
  },
  UPDATE_PROFILE_PHONE(state, phone) {
    state.phone = phone;
  },
  NUM_OF_BOOK_UPDATED(state, data) {
    state.numOfKeepingBooks = data.keeping;
    state.numOfRequestingBooks = data.requesting;
    state.numOfReturningBooks = data.returning;
  },

  // Notification
  ADD_NOTIFICATION(state, notification) {
    const newList = state.notifications.concat(notification);
    newList.sort((a, b) => moment(b.time).unix() - moment(a.time).unix());

    state.notifications = newList;
  },

  RESET_NOTIFICATION(state) {
    state.notifications = [];
  },

  OPEN_TRANSACTION_POPUP(state, tx) {
    state.transactionPopupOpen = true;
    state.transactionPopupTx = tx;
  },
  CLOSE_TRANSACTION_POPUP(state) {
    state.transactionPopupOpen = false;
    state.transactionPopupTx = null;
  },

  OPEN_DETAILS_POPUP(state, detail) {
    state.detailsPopupOpen = true;
    state.detailsPopupData = detail;
  },
  SET_ASSET_DATA(state, data) {
    state.detailsPopupAssetData = data;
  },
  CLOSE_DETAILS_POPUP(state) {
    state.detailsPopupOpen = false;
    state.detailsPopupData = null;
  },
  UPDATE_LOADING(state, data) {
    state.loading = data;
  }
};

export default mutations;
