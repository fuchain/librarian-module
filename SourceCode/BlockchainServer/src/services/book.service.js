import asset from "@core/bigchaindb/asset";
import { db } from "@models";

async function searchBook(id) {
    return await asset.searchAsset(id);
}

async function getAllBookDetail() {
    const bookDetailCollection = db.collection("book_details");
    const listBookDetails = await bookDetailCollection
        .find()
        .limit(50)
        .toArray();

    return listBookDetails;
}

async function searchBookDetail(text) {
    const bookDetailCollection = db.collection("book_details");

    // Find if text index is not existed to create one
    await bookDetailCollection.createIndex({
        name: "text"
    });

    const listBookDetails = await bookDetailCollection
        .find({ $text: { $search: text } })
        .limit(50)
        .toArray();

    return listBookDetails;
}

async function getBookDetail(id) {
    const bookDetailCollection = db.collection("book_details");

    if (isNaN(id)) {
        return null;
    }

    const idInt = parseInt(id);

    const bookDetail = await bookDetailCollection.findOne({
        id: idInt
    });

    return bookDetail;
}

export default {
    searchBook,
    getAllBookDetail,
    searchBookDetail,
    getBookDetail
};
