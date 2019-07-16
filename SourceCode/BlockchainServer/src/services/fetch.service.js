import asset from "@core/bigchaindb/asset";
import {
    db
} from "@models"

async function getBookDetail(assetId) {
    const transactionList = await asset.getAsset(assetId);

    if (!transactionList) {
        return null;
    }

    const bookDetailId = transactionList[0].asset.data.book_detail;
    if (isNaN(bookDetailId)) {
        return null;
    }

    const bookDetailCollection = db.collection("book_details");
    const bookDetail = await bookDetailCollection.findOne({
        id: parseInt(bookDetailId)
    });

    if (!bookDetail) {
        return null;
    }
    return bookDetail;
}

async function getEmail(publicKey) {
    const email = await asset.getEmailFromPublicKey(publicKey);
    if(!email){
        return null;
    }

    return email;
}
export default {
    getBookDetail,
    getEmail
};
