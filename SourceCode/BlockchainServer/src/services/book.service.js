import asset from "@core/bigchaindb/asset";
import { db } from "@models";
import env from "@core/env";
import algoliaSearch from "algoliasearch";

async function searchBook(id) {
    return await asset.searchAsset(id);
}

async function getAllBookDetail() {
    const bookDetailCollection = db.collection("book_details");
    const listBookDetails = await bookDetailCollection
        .find()
        // .limit(50)
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

async function getBookInstanceList(bookDetailId, notKeptByLibrarian = false) {
    const bookDetailIdSearch = await asset.searchAsset(bookDetailId);

    const bookList = bookDetailIdSearch.filter(e => e.data.book_detail);
    const bookListWithCurrentKeeperPromises = bookList.map(async e => {
        const email = await getCurrenOwnerOfABook(e.id);
        return {
            asset_id: e.id,
            current_keeper: email || null
        };
    });

    if (notKeptByLibrarian) {
        const librarianEmail = await asset.getEmailFromPublicKey(env.publicKey);
        const bookListWithCurrentKeeper = await Promise.all(
            bookListWithCurrentKeeperPromises
        );

        return bookListWithCurrentKeeper.filter(
            e => e.current_keeper !== librarianEmail
        );
    }

    return await Promise.all(bookListWithCurrentKeeperPromises);
}

async function getCurrenOwnerOfABook(assetId) {
    try {
        const txs = await asset.getAsset(assetId);

        if (!txs.length) {
            return null;
        }

        const publicKey = txs[txs.length - 1].outputs[0].public_keys[0];

        if (!publicKey) {
            return null;
        }

        const email = await asset.getEmailFromPublicKey(publicKey);

        if (!email) {
            return null;
        }

        return email;
    } catch (err) {
        console.log(err);
        return null;
    }
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

async function getBookTotalAtLib(bookDetailId) {
    const bookList = await getBookInstanceList(bookDetailId);

    const remainBookList = await bookList.filter(async book => {
        const transactionList = await asset.getAsset(book.id);
        if (transactionList.length) {
            const publicKey = transactionList[0].outputs[0].public_keys[0];
            if (publicKey === env.publicKey) {
                return true;
            }
        }
        return false;
    });
    return remainBookList.length;
}

export default {
    searchBook,
    getAllBookDetail,
    searchBookDetail,
    getBookDetail,
    getBookInstanceList,
    getHistoryOfBookInstance,
    getBookInstanceTotal,
    getBookTotalAtLib
};
