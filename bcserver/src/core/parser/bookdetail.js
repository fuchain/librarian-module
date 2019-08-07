import { db } from "@models";
import rejectService from "@services/reject.service";

export async function fillBookInfo(bookArr, bookFieldInArr = "book_detail") {
    const bookDetailIds = bookArr.map(book => parseInt(book[bookFieldInArr]));

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

    const promises = bookArr.map(async book => {
        book[bookFieldInArr] = bookDetailIdArr.find(
            e => e.id === parseInt(book[bookFieldInArr])
        );

        book["reject_count"] = book.asset_id
            ? await rejectService.getRejectCount(book.asset_id)
            : 0;

        return book;
    });

    return await Promise.all(promises);
}
