import $http from "@http";

const actions = {
  async getBooks({ commit }) {
    try {
      const { data } = await $http.get(`${$http.baseUrl}/users/current_books`);
      commit("BOOKS_UPDATED", data);
    } catch (e) {
      // Catch error
      console.log(e);
    }
  }
};

export default actions;
