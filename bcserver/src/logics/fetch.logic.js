import asset from "@core/fuchain/asset";
import { db } from "@core/db";
import env from "@core/env";

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
    if (publicKey === env.publicKey) {
        const librarianEmail = "librarian@fptu.tech";
        return librarianEmail;
    }

    const email = await asset.getEmailFromPublicKey(publicKey);
    if (!email) {
        return null;
    }

    return email;
}

export default {
    getBookDetail,
    getEmail
};
