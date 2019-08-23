const mutations = {
  DATA_LIST_UPDATED(state, data) {
    data.sort((a, b) => b.id - a.id);

    data = data.map(e => {
      e.inactive = !e.inactive;
      return e;
    });

    state.users = data;
    state.loaded = true;
  }
};

export default mutations;
