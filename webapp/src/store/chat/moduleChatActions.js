export default {
  updateAboutChat({ commit }, value) {
    commit("UPDATE_ABOUT_CHAT", value);
  },
  updateStatusChat({ commit }, value) {
    commit("UPDATE_STATUS_CHAT", value, { root: true });
  },
  setChatSearchQuery({ commit }, query) {
    commit("SET_CHAT_SEARCH_QUERY", query);
  },
  sendChatMessage({ getters, commit }, payload) {
    payload.chatData = getters.chatDataOfUser(payload.id);
    commit("SEND_CHAT_MESSAGE", payload);
  },
  toggleIsPinned({ commit }, payload) {
    commit("TOGGLE_IS_PINNED", payload);
  },
  markSeenAllMessages({ getters, commit }, id) {
    let payload = { id: id };
    payload.chatData = getters.chatDataOfUser(payload.id);
    commit("MARK_SEEN_ALL_MESSAGES", payload);
  }
};
