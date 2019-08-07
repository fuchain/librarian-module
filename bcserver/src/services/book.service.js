import asset from "@core/fuchain/asset";
import { db } from "@models";
import env from "@core/env";
import concurrencyHandler from "@core/handlers/concurrency.handler";
import algoliaSearch from "algoliasearch";
import rejectService from "@services/reject.service";

async function searchBook(id) {
    return await asset.searchAsset(id);
}

async function getAllBookDetail() {
    const bookDetailCollection = db.collection("book_details");
    const listBookDetails = await bookDetailCollection
        .find()
        .limit(50)
        .sort({ amount: -1 })
        .toArray();

    return listBookDetails;
}

async function searchBookDetail(text) {
    const client = algoliaSearch(
        "CPSMLNMIK1",
        "c37812a2aa6540a3c4d7ceb3adcf4721"
    );
    const index = client.initIndex("dev_LIBRARIAN");

    const { hits } = await index.search(text);

    return hits;
}

async function getBookDetailIdOfAssetId(assetId) {
    const assetList = await asset.getAsset(assetId);
    const publicKey = assetList[assetList.length - 1].outputs[0].public_keys[0];
    const email = await asset.getEmailFromPublicKey(publicKey);
    const bookDetailId = parseInt(assetList[0].asset.data.book_detail);
    const bookDetail = await getBookDetail(bookDetailId);
    return { bookDetail, email };
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
    const bookDetailIdSearch = await asset.searchAsset(bookDetailId);
    const bookList = bookDetailIdSearch.filter(e => e.data.book_detail);

    const result = bookList.map(e => {
        return {
            asset_id: e.id,
            transaction_count: "Fetching",
            current_keeper: "Fetching"
        };
    });

    return result;
}

async function getBookInstanceDetailList(bookDetailId) {
    const bookDetailIdSearch = await asset.searchAsset(bookDetailId);
    const bookList = bookDetailIdSearch.filter(e => e.data.book_detail);

    const result = await concurrencyHandler(bookList, 3, async e => {
        const { count, email } = await getDetailInformationOfABook(e.id);
        return {
            asset_id: e.id,
            transaction_count: count,
            current_keeper: email || null
        };
    });

    return result;
}

async function getDetailInformationOfABook(assetId) {
    try {
        const txs = await asset.getAsset(assetId);

        if (!txs.length) {
            return {
                count: "Transaction array is null",
                email: "Transaction array is null"
            };
        }

        const publicKey = txs[txs.length - 1].outputs[0].public_keys[0];

        if (!publicKey) {
            return {
                count: "Invalid transaction",
                email: "Invalid transaction"
            };
        }

        const email = await asset.getEmailFromPublicKey(publicKey);

        if (!email) {
            return {
                count: "Invalid key: " + publicKey,
                email: "Invalid key: " + publicKey
            };
        }

        return {
            count: txs.length - 1,
            email
        };
    } catch (err) {
        return {
            count: "Network Error",
            email: "Network Error"
        };
    }
}

async function getHistoryOfBookInstance(bookID) {
    const allTxs = await asset.getAssetTransactions(bookID);
    if (!allTxs.length) {
        return [];
    }

    const txs = allTxs.filter(tx => tx.operation !== "CREATE");

    const promises = txs.map(async tx => {
        const assetId = tx.operation === "CREATE" ? tx.id : tx.asset.id;

        try {
            const returnerEmail = await asset.getEmailFromPublicKey(
                tx.inputs[0].owners_before[0]
            );
            const receiverEmail = await asset.getEmailFromPublicKey(
                tx.outputs[0].public_keys[0]
            );

            const transferDate =
                (tx.metadata && tx.metadata.transfer_date) || null;

            return {
                id: tx.id,
                returner: returnerEmail,
                receiver: receiverEmail,
                transfer_date: transferDate
            };
        } catch (err) {
            return {
                id: tx.id,
                returner: tx.inputs[0].owners_before[0],
                receiver: tx.outputs[0].public_keys[0],
                asset_id: assetId
            };
        }
    });

    const result = await Promise.all(promises); // This is sorted by time

    const rejectHistory = await rejectService.getRejectTransactionOfABook(
        bookID
    ); // Rejects

    return result.concat(rejectHistory);
}

async function getBookInstanceTotal(type) {
    const bookList = await asset.searchAsset(type);
    if (!bookList.length) {
        return 0;
    }
    return bookList.length;
}

async function getBookAtLib(bookDetailId) {
    const bookList = await getBookInstanceList(bookDetailId);

    const remainBookList = await concurrencyHandler(bookList, 1, async book => {
        const transactionList = await asset.getAsset(book.asset_id);

        const firstPublicKey = transactionList[0].outputs[0].public_keys[0];
        if (firstPublicKey !== env.publicKey) {
            return null;
        }

        if (transactionList.length && transactionList.length > 1) {
            const lastPublicKey =
                transactionList[transactionList.length - 1].outputs[0]
                    .public_keys[0];

            if (lastPublicKey !== env.publicKey) {
                return null;
            }
        }

        return book;
    });

    return remainBookList.filter(e => e !== null);
}

async function getBookTotalAtLib(bookDetailId) {
    const remainBookList = await getBookAtLib(bookDetailId);

    return remainBookList.length;
}

export default {
    searchBook,
    getAllBookDetail,
    searchBookDetail,
    getBookDetail,
    getBookInstanceList,
    getBookInstanceDetailList,
    getHistoryOfBookInstance,
    getBookInstanceTotal,
    getBookAtLib,
    getBookTotalAtLib,
    getBookDetailIdOfAssetId
};
