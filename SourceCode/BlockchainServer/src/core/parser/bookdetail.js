import { db } from "@models";

export async function fillBookInfo(bookArr) {
    try {
        const bookDetailIds = await bookArr.map(book =>
            parseInt(book.book_detail)
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
            return {
                book_id: book.book_id,
                asset_id: book.asset_id,
                book_detail: bookDetailIdArr.find(
                    e => e.id === parseInt(book.book_detail)
                )
            };
        });
    } catch (err) {
        throw err;
    }
}
