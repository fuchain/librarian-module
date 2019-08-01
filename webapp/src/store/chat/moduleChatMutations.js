/* eslint-disable standard/computed-property-even-spacing */
import Vue from "vue";

export default {
  UPDATE_ABOUT_CHAT(state, value) {
    state.AppActiveUser.about = value;
  },
  SET_CHAT_SEARCH_QUERY(state, query) {
    state.chatSearchQuery = query;
  },
  SEND_CHAT_MESSAGE(state, payload) {
    if (payload.chatData) {
      state.chats[
        Object.keys(state.chats).find(key => key === payload.id)
      ].msg.push(payload.msg);
    } else {
      const chatId = payload.id;
      Vue.set(state.chats, [chatId], {
        isPinned: payload.isPinned,
        msg: [payload.msg]
      });
    }
  },
  TOGGLE_IS_PINNED(state, payload) {
    state.chats[
      Object.keys(state.chats).find(key => key === payload.id)
    ].isPinned = payload.value;
  },
  MARK_SEEN_ALL_MESSAGES(state, payload) {
    payload.chatData.msg.forEach(msg => {
      msg.isSeen = true;
    });
  }
};
