const mutations = {
  DATA_LIST_UPDATED(state, data) {
    data.sort((a, b) => b.id - a.id);

    state.users = data;
    state.loaded = true;
  }
};

export default mutations;
