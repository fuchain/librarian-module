import $http from "@http";

const actions = {
  // ////////////////////////////////////////////
  // SIDEBAR & UI UX
  // ////////////////////////////////////////////

  updateSidebarWidth({ commit }, width) {
    commit("UPDATE_SIDEBAR_WIDTH", width);
  },
  updateI18nLocale({ commit }, locale) {
    commit("UPDATE_I18N_LOCALE", locale);
  },
  toggleContentOverlay({ commit }) {
    commit("TOGGLE_CONTENT_OVERLAY");
  },
  updateTheme({ commit }, val) {
    commit("UPDATE_THEME", val);
  },
  openSidebar({ commit }) {
    commit("TOGGLE_IS_SIDEBAR_ACTIVE", true);
  },

  // ////////////////////////////////////////////
  // COMPONENT
  // ////////////////////////////////////////////

  // VxAutoSuggest
  updateStarredPage({ commit }, payload) {
    commit("UPDATE_STARRED_PAGE", payload);
  },

  //  The Navbar
  arrangeStarredPagesLimited({ commit }, list) {
    commit("ARRANGE_STARRED_PAGES_LIMITED", list);
  },
  arrangeStarredPagesMore({ commit }, list) {
    commit("ARRANGE_STARRED_PAGES_MORE", list);
  },

  // Profile
  async getProfile({ commit }) {
    try {
      const { data } = await $http.post(`${$http.baseUrl}/user/profile`);
      commit("PROFILE_UPDATED", data);
    } catch (e) {
      console.log(e);

      // Catch error
      if (e.response && e.response.status && e.response.status === 401) {
        // Logout
        window.vue.$auth.clearAuth();
        window.vue.$router.push("/login");
      }

      throw e;
    }
  },

  // Update profile
  async updateProfile({ commit }, { fullname, phone }) {
    try {
      const { data } = await $http.post(
        `${$http.baseUrl}/user/update_profile`,
        {
          fullname,
          phone
        }
      );
      commit("PROFILE_UPDATED_SUCCESS", data);
    } catch (e) {
      // Catch error
      throw e;
    }
  },

  // Get num of books
  async getNumOfBooks({ commit }) {
    try {
      const { data } = await $http.post(`${$http.baseUrl}/user/keeping_amount`);
      commit("NUM_OF_BOOK_UPDATED", data);
    } catch (e) {
      // Catch error
      throw e;
    }
  },

  // Notification
  addNotification({ commit }, notification) {
    commit("ADD_NOTIFICATION", notification);
  },

  // Get notification
  async getNotification({ commit }, reset) {
    if (reset) {
      commit("RESET_NOTIFICATION");
    }

    try {
      const { data } = await $http.get(`${$http.nodeUrl}/notifications`);

      data.map(e => {
        const notification = {
          isRead: e.isRead,
          title: "Thông báo",
          message: e.message,
          id: e._id,
          type: e.type,
          icon: "InfoIcon",
          time: e.createdAt,
          category: "primary"
        };

        commit("ADD_NOTIFICATION", notification);
      });
    } catch (e) {
      // Catch error
      throw e;
    }
  },

  // Open Transaction Popup
  async openTxPopup({ commit }, tx) {
    commit("OPEN_TRANSACTION_POPUP", tx);
  },
  async closeTxPopup({ commit }, time = 0) {
    setTimeout(function() {
      commit("CLOSE_TRANSACTION_POPUP");
    }, time);
  },

  // Open Detail Popup
  async openDetailsPopup({ commit }, details, assetId) {
    console.log(assetId);
    commit("OPEN_DETAILS_POPUP", details, assetId);
  },
  async setAssetData({ commit }, data) {
    commit("SET_ASSET_DATA", data);
  },
  async closeDetailsPopup({ commit }, time = 0) {
    setTimeout(function() {
      commit("CLOSE_DETAILS_POPUP");
    }, time);
  }
};

export default actions;
