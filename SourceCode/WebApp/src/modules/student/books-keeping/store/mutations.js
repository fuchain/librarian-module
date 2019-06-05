const mutations = {
  BOOKS_UPDATED(state, data) {
    const books = data.map(e => {
      return {
        id: e.id,
        name: e.bookDetail.name,
        description: `Book ${
          e.bookDetail.name
        } for Software Engineering learning at FPT University`,
        image: "/images/book-thumbnail.jpg",
        time: e.updateDate,
        code: e.bookDetail.name.substring(0, 3).toUpperCase() + "101"
      };
    });

    state.books = books;
    state.loaded = true;
  }
};

export default mutations;
