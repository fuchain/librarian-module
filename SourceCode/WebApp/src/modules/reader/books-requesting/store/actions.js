import $http from "@http";

const actions = {
  async getBooks({ commit }) {
    try {
      const { data } = await $http.post(`${$http.baseUrl}/user/requesting`);
      commit("BOOKS_UPDATED", data);
    } catch (e) {
      // Catch error
      throw e;
    }
  }
};

export default actions;
