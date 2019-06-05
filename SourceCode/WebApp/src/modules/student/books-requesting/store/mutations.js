const mutations = {
  BOOKS_UPDATED(state, data) {
    const books = data.map(e => {
      return {
        id: e.bookDetail.id,
        name: e.bookDetail.name,
        description: `Book ${
          e.bookDetail.name
        } for Software Engineering learning at FPT University`,
        image: "https://i.imgur.com/2j6B1n5.jpg",
        code: e.bookDetail.name.substring(0, 3).toUpperCase() + "101",
        status: e.status
      };
    });

    state.books = books;
    state.loaded = true;
  }
};

export default mutations;
