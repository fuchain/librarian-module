import $http from "@http";

const actions = {
  async getBookDetails({ commit }) {
    try {
      const { data } = await $http.get(
        `${$http.baseUrl}/librarian/book_details`
      );
      commit("DATA_LIST_UPDATED", data);
    } catch (e) {
      // Catch error
      throw e;
    }
  },
  async searchBookDetails({ commit }, text) {
    try {
      const { data } = await $http.get(
        `${$http.baseUrl}/librarian/book_details?name=${text}`
      );
      commit("DATA_LIST_UPDATED", data);
    } catch (e) {
      // Catch error
      throw e;
    }
  }
};

export default actions;
