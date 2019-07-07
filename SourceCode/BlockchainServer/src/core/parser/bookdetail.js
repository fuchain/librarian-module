import { db } from "@models";

export async function fillBookInfo(bookArr, bookFieldInArr = "book_detail") {
    try {
        const bookDetailIds = await bookArr.map(book =>
            parseInt(book[bookFieldInArr])
        );

        const bookDetailsCollection = db.collection("book_details");
        const bookDetailIdArr = await bookDetailsCollection
            .find(
                {
                    id: {
                        $in: bookDetailIds
                    }
                },
                {
                    projection: {
                        _id: 0,
                        amount: 0
                    }
                }
            )
            .toArray();

        return bookArr.map(book => {
            book[bookFieldInArr] = bookDetailIdArr.find(
                e => e.id === parseInt(book[bookFieldInArr])
            );

            return book;
        });
    } catch (err) {
        throw err;
    }
}
