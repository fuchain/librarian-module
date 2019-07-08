import outputService from "@services/output.service";
import asset from "@core/bigchaindb/asset";
import transaction from "@core/bigchaindb/transaction";
import { fillBookInfo } from "@core/parser/bookdetail";
import { db } from "@models";

async function getProfile(publicKey) {
    throw new Error("hehe");
    const listAssets = await asset.searchPublicKey(publicKey);
    if (listAssets.length) {
        return await asset.getAsset(listAssets[0].id);
    }

    return null;
}

async function getCurrentBook(publicKey) {
    try {
        const transactionIds = await outputService.getUnspent(publicKey);
        const promises = transactionIds.map(e => {
            const transactionId = e.transaction_id;
            const listAssets = asset.getBookFromTransactionId(transactionId);

            return listAssets;
        });

        const result = await Promise.all(promises);
        const bookDetailFill = await fillBookInfo(result);

        return bookDetailFill.filter(book => book.book_detail);
    } catch (err) {
        throw err;
    }
}

async function getInQueueBook(publicKey, isGetReturning = true) {
    try {
        const email = await asset.getEmailFromPublicKey(publicKey);

        const matchingCollection = db.collection("matchings");
        const inQueueBooks = await matchingCollection
            .find({
                email,
                bookId: {
                    $exists: isGetReturning ? true : false
                }
            })
            .toArray();

        const bookDetailFill = await fillBookInfo(inQueueBooks, "bookDetailId");

        return bookDetailFill;
    } catch (err) {
        throw err;
    }
}

async function getTransferHistory(publicKey) {
    try {
        const txIds = await outputService.getSpent(publicKey);
        const promises = txIds.map(e => {
            const txId = e.transaction_id;
            const tx = transaction.get(txId);

            return tx;
        });

        const result = await Promise.all(promises);

        return result.map(tx => {
            const assetId = tx.operation === "CREATE" ? tx.id : tx.asset.id;

            return {
                id: tx.id,
                returner: tx.inputs[0].owners_before[0],
                receiver: tx.outputs[0].public_keys[0],
                operation: tx.operation,
                asset_id: assetId
            };
        });
    } catch (err) {
        throw err;
    }
}

export default {
    getProfile,
    getCurrentBook,
    getInQueueBook,
    getTransferHistory
};
