import Vue from "vue";
import Vuex from "vuex";

import state from "./state";
import getters from "./getters";
import mutations from "./mutations";
import actions from "./actions";

import moduleChat from "./chat/moduleChat.js";
import moduleCalendar from "./calendar/moduleCalendar.js";

Vue.use(Vuex);

export default new Vuex.Store({
  getters,
  mutations,
  state,
  actions,
  modules: {
    chat: moduleChat,
    calendar: moduleCalendar
  },
  strict: process.env.NODE_ENV !== "production"
});
