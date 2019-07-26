import { parseBookItem } from "@http/parse";

const mutations = {
  BOOKS_UPDATED(state, data) {
    const books = parseBookItem(data);

    state.books = books;
    state.loaded = true;
  }
};

export default mutations;
