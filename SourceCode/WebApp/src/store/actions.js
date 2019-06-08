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
      const { data } = await $http.get(`${$http.baseUrl}/users/profile`);
      commit("PROFILE_UPDATED", data);
    } catch (e) {
      // Catch error
      throw e;
    }
  },

  // Update profile
  async updateProfile({ commit }, { fullname, phone }) {
    try {
      const { data } = await $http.put(
        `${$http.baseUrl}/users/update_profile`,
        { fullname, phone }
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
      const { data } = await $http.get(`${$http.baseUrl}/users/book_infos`);
      commit("NUM_OF_BOOK_UPDATED", data);
    } catch (e) {
      // Catch error
      throw e;
    }
  },

  // Add coin
  addCoin({ commit }, amount) {
    commit("ADD_COIN", amount);
  }
};

export default actions;
