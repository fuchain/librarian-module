import $http from "@http";

const actions = {
  async getUsers({ commit }) {
    try {
      const { data } = await $http.get(`${$http.baseUrl}/librarian/users`);
      commit(
        "DATA_LIST_UPDATED",
        data.filter(e => !e.email.includes("librarian"))
      );
    } catch (e) {
      // Catch error
      throw e;
    }
  }
};

export default actions;
