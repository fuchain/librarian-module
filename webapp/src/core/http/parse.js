export function parseBookItem(data) {
  return data.map(e => {
    const bookDetailField = e.book_detail || e.bookDetailId;
    const bookDetailId = e.book_detail ? e.book_detail.id : e.bookDetailId.id;
    return {
      id: e.asset_id || e.bookId,
      bookDetailId,
      name: bookDetailField.name,
      description: bookDetailField.description,
      image: bookDetailField.thumbnail || "/images/book-thumbnail.jpg",
      time: e.transfer_time ? new Date(e.transfer_time * 1000) : 0,
      code:
        (bookDetailField.subject_codes &&
          bookDetailField.subject_codes.length &&
          bookDetailField.subject_codes[0]) ||
        null,
      status: "in use",
      matched: e.matched,
      user: e.matchWith || 0,
      phone: e.matchPhone || 0,
      type: e.book_detail ? "keeping" : "matching",
      details: bookDetailField,
      rejectCount: e.reject_count || 0
    };
  });
}

export function parseBookSearchItem(data) {
  return data.map(e => {
    return {
      id: e.id,
      name: e.name,
      description: e.description,
      image: e.thumbnail || "/images/book-thumbnail.jpg",
      code:
        e.subject_codes && e.subject_codes.length && e.subject_codes[0]
          ? e.subject_codes[0]
          : null,
      type: "info",
      details: e
    };
  });
}

export function parseSingleItem(e) {
  return {
    id: e.id,
    name: e.name,
    description: e.description,
    image: e.thumbnail || "/images/book-thumbnail.jpg",
    code:
      e.subject_codes && e.subject_codes.length && e.subject_codes[0]
        ? e.subject_codes[0]
        : null,
    type: "info",
    details: e
  };
}
