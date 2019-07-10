export function parseBookItem(data, type = "defaut") {
  return data.map(e => {
    const bookDetailField = e.book_detail || e.bookDetailId;
    return {
      id: e.asset_id,
      name: bookDetailField.name,
      description: bookDetailField.description,
      image: bookDetailField.thumbnail || "/images/book-thumbnail.jpg",
      time: new Date(),
      code:
        (bookDetailField.subject_codes &&
          bookDetailField.subject_codes.length &&
          bookDetailField.subject_codes[0]) ||
        "N/A",
      status: "in use",
      user: e.match_with,
      requestId: e.request_id,
      type
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
          : "N/A",
      type: "info"
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
        : "N/A",
    type: "info"
  };
}
