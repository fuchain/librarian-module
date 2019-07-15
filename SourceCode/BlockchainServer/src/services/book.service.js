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
        .find({
            $text: {
                $search: text
            }
        })
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

async function getBookInstanceList(bookDetailId) {
    const bookList = await asset.searchAsset(bookDetailId);

    return bookList.filter(e => e.data.book_detail);
}

async function getHistoryOfBookInstance(bookID) {
    const assetIds = await asset.searchAsset(bookID);
    if (!assetIds.length) {
        return null;
    }

    const assetId = assetIds[0].id;
    return await asset.getAssetTransactions(assetId);
}

async function getBookInstanceTotal(type) {
    const bookList = await asset.searchAsset(type);
    if (!bookList.length) {
        return 0;
    }
    return bookList.length;
}

export default {
    searchBook,
    getAllBookDetail,
    searchBookDetail,
    getBookDetail,
    getBookInstanceList,
    getHistoryOfBookInstance,
    getBookInstanceTotal
};
