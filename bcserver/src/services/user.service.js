import outputService from "@services/output.service";
import fetchService from "@services/fetch.service";
import asset from "@core/fuchain/asset";
import transaction from "@core/fuchain/transaction";
import { fillBookInfo } from "@core/parser/bookdetail";
import { db } from "@models";
import env from "@core/env";

async function getProfile(email) {
    // If librarian
    if (email === "librarian@fptu.tech") {
        return {
            email: "librarian@fptu.tech",
            type: "librarian",
            fullname: "Thủ thư",
            phone: "0123456789"
        };
    }

    const userCollection = db.collection("users");
    const userInDB = await userCollection.findOne({
        email
    });

    if (!userInDB) {
        userCollection.insertMany([
            {
                email,
                fullname: null,
                phone: null
            }
        ]);
    }

    const phone = userInDB && userInDB.phone;
    const fullname = userInDB && userInDB.fullname;

    return {
        email,
        type: "reader",
        fullname,
        phone
    };
}

async function updateProfile(email, fullname, phone) {
    const phoneInt = parseInt(phone);
    if (isNaN(phoneInt) || phoneInt.toString().length < 9) {
        throw new Error("Not valid phone number");
    }

    if (!fullname) {
        throw new Error("Not valid full name");
    }

    const userCollection = db.collection("users");
    await userCollection.updateOne(
        {
            email
        },
        {
            $set: {
                fullname: fullname,
                phone: phone
            }
        }
    );

    return {
        fullname,
        phone
    };
}

async function getCurrentBook(publicKey, getAll = false) {
    // Not for librarian
    if (publicKey === env.publicKey) {
        return [];
    }

    const transactionIds = await outputService.getUnspent(publicKey);
    const promises = transactionIds.map(async e => {
        const transactionId = e.transaction_id;
        const assetDetail = await asset.getBookFromTransactionId(transactionId);
        assetDetail.transfer_time = await getLastTransactionTime(
            assetDetail.asset_id
        );

        return assetDetail;
    });

    const result = await Promise.all(promises);

    const bookDetailFill = await fillBookInfo(result);

    const currentKeepingBooks = bookDetailFill.filter(book => book.book_detail);

    if (getAll) {
        return currentKeepingBooks;
    }

    const returningBooks = await getInQueueBook(publicKey);
    const currentBooks = currentKeepingBooks.filter(e => {
        const find = returningBooks.find(f => f.bookId === e.asset_id);
        return find ? false : true;
    });

    return currentBooks;
}

async function getInQueueBook(email, isGetReturning = true) {
    const matchingCollection = db.collection("matchings");

    const inQueueBooks = isGetReturning
        ? await matchingCollection
              .find({
                  email,
                  bookId: {
                      $ne: null
                  }
              })
              .toArray()
        : await matchingCollection
              .find({
                  email,
                  bookId: null
              })
              .toArray();

    const bookDetailFill = await fillBookInfo(inQueueBooks, "bookDetailId");

    return bookDetailFill;
}

async function getTransferHistory(publicKey) {
    const txIdsSpent = await outputService.getSpent(publicKey);
    const txIdsUnspent = await outputService.getUnspent(publicKey);
    const txIds = txIdsSpent.concat(txIdsUnspent);

    const promises = txIds.map(async e => {
        const txId = e.transaction_id;
        const tx = await transaction.get(txId);

        return tx;
    });

    const result = await Promise.all(promises);

    const txItems = result.map(async tx => {
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

            const bookDetail = await fetchService.getBookDetail(assetId);

            return {
                id: tx.id,
                returner: returnerEmail,
                receiver: receiverEmail,
                operation: tx.operation,
                asset_id: assetId,
                transfer_date: transferDate,
                book_detail: bookDetail
            };
        } catch (err) {
            return {
                id: tx.id,
                returner: tx.inputs[0].owners_before[0],
                receiver: tx.outputs[0].public_keys[0],
                operation: tx.operation,
                asset_id: assetId
            };
        }
    });

    return await Promise.all(txItems);
}

async function getAllUsers(type = "reader") {
    const users = await asset.getAllUsers(type);

    const usersCollection = db.collection("users");

    const listPromises = users.map(async user => {
        if (user.data.email.includes("librarian")) {
            return {
                email: user.data.email,
                public_key: null,
                full_name: null,
                phone: null
            };
        }

        const assetTxs = await asset.getAsset(user.id);
        const latestTx = assetTxs[assetTxs.length - 1];
        const publicKey = latestTx.metadata.public_key;

        const localUser = await usersCollection.findOne({
            email: user.data.email
        });

        if (!localUser) {
            return {
                email: user.data.email,
                public_key: publicKey
            };
        }

        return {
            email: user.data.email,
            public_key: publicKey,
            full_name: localUser.fullname,
            phone: localUser.phone
        };
    });

    const data = await Promise.all(listPromises);
    return data;
}

async function getUserTotal(type) {
    const userList = await asset.searchAsset(type);
    if (!userList.length) {
        return 0;
    }
    return userList.length;
}

async function getPhoneFromEmail(email) {
    const userCollection = db.collection("users");
    const userInDB = await userCollection.findOne({
        email
    });

    if (userInDB) {
        return userInDB.phone;
    }

    return null;
}

async function getLastTransactionTime(assetId) {
    const txs = await asset.getAssetTransactions(assetId);
    if (!txs || !txs.length || txs.length === 1) {
        return 0;
    }

    return txs[txs.length - 1].metadata.transfer_date;
}

export default {
    getProfile,
    updateProfile,
    getCurrentBook,
    getInQueueBook,
    getTransferHistory,
    getAllUsers,
    getUserTotal,
    getPhoneFromEmail,
    getLastTransactionTime
};
