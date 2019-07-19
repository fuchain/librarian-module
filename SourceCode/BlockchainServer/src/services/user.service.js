import outputService from "@services/output.service";
import asset from "@core/bigchaindb/asset";
import transaction from "@core/bigchaindb/transaction";
import { fillBookInfo } from "@core/parser/bookdetail";
import { db } from "@models";
import env from "@core/env";

async function getProfile(publicKey) {
    // If librarian
    if (publicKey === env.publicKey) {
        return {
            email: "librarian@fptu.tech",
            type: "librarian",
            fullname: "Thủ thư",
            phone: "0123456789"
        };
    }

    const listAssets = await asset.searchPublicKey(publicKey);
    if (listAssets.length) {
        const found = await asset.getAsset(listAssets[0].id);

        const email = found[0].asset.data.email;
        const type = found[0].asset.data.type;

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
            type,
            fullname,
            phone
        };
    }

    return null;
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

async function getCurrentBook(publicKey) {
    const transactionIds = await outputService.getUnspent(publicKey);
    const promises = transactionIds.map(e => {
        const transactionId = e.transaction_id;
        const listAssets = asset.getBookFromTransactionId(transactionId);

        return listAssets;
    });

    const result = await Promise.all(promises);

    const bookDetailFill = await fillBookInfo(result);

    const currentKeepingBooks = bookDetailFill.filter(book => book.book_detail);
    const returningBooks = await getInQueueBook(publicKey);

    const currentBooks = currentKeepingBooks.filter(e => {
        const find = returningBooks.find(f => f.bookId === e.asset_id);
        return find ? false : true;
    });

    return currentBooks;
}

async function getInQueueBook(publicKey, isGetReturning = true) {
    const email = await asset.getEmailFromPublicKey(publicKey);

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
    const txIds = await outputService.getSpent(publicKey);
    const promises = txIds.map(e => {
        const txId = e.transaction_id;
        const tx = transaction.get(txId);

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

            return {
                id: tx.id,
                returner: returnerEmail,
                receiver: receiverEmail,
                operation: tx.operation,
                asset_id: assetId,
                transfer_date: transferDate
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

async function getAllUsers(type) {
    const users = await asset.getAllUsers(type);

    const listPromises = users.map(async user => {
        const assetTxs = await asset.getAsset(user.id);
        const createTx = assetTxs[0];
        const publicKey = createTx.metadata.public_key;

        return {
            email: user.data.email,
            public_key: publicKey
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

export default {
    getProfile,
    updateProfile,
    getCurrentBook,
    getInQueueBook,
    getTransferHistory,
    getAllUsers,
    getUserTotal,
    getPhoneFromEmail
};
