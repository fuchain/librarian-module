import outputService from "@services/output.service";
import asset from "@core/bigchaindb/asset";
import transaction from "@core/bigchaindb/transaction";
import {
    fillBookInfo
} from "@core/parser/bookdetail";
import {
    db
} from "@models";
import util from "util"

async function getProfile(publicKey) {
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
            userCollection.insertMany([{
                email,
                fullname: null,
                phone: null
            }]);
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
    await userCollection.updateOne({
        email
    }, {
        $set: {
            fullname: fullname,
            phone: phone
        }
    });

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

    return bookDetailFill.filter(book => book.book_detail);
}

async function getInQueueBook(publicKey, isGetReturning = true) {
    const email = await asset.getEmailFromPublicKey(publicKey);

    const matchingCollection = db.collection("matchings");
    const inQueueBooks = await matchingCollection
        .find({
            email,
            bookId: {
                $ne: isGetReturning ? null : false
            }
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

export default {
    getProfile,
    updateProfile,
    getCurrentBook,
    getInQueueBook,
    getTransferHistory,
    getAllUsers
};
