const mutations = {
  DATA_LIST_UPDATED(state, data) {
    state.bookDetails = data;
    state.loaded = true;
  }
};

export default mutations;
