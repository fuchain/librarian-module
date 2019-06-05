import $http from "@http";

const actions = {
  async getBooks({ commit }) {
    try {
      const { data } = await $http.get(
        `${$http.baseUrl}/requests/get_list?type=2`
      );
      commit("BOOKS_UPDATED", data);
    } catch (e) {
      // Catch error
      throw e;
    }
  }
};

export default actions;
