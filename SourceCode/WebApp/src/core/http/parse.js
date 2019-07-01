export function parseBookItem(data, type = "defaut") {
  return data.map(e => {
    return {
      id: e.id,
      name: e.bookDetail.name,
      description: e.bookDetail.description,
      image: e.bookDetail.thumbnail || "/images/book-thumbnail.jpg",
      time: new Date(e.updateDate * 1000),
      code:
        (e.bookDetail.parseedSubjectCode &&
          e.bookDetail.parseedSubjectCode.length &&
          e.bookDetail.parseedSubjectCode[0]) ||
        "N/A",
      status: e.status,
      user: e.pairedUser,
      requestId: e.id,
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
        e.parseedSubjectCode &&
        e.parseedSubjectCode.length &&
        e.parseedSubjectCode[0]
          ? e.parseedSubjectCode[0]
          : "N/A",
      type: "info"
    };
  });
}
