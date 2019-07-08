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

export default { searchBook, getAllBookDetail };
