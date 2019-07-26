import transaction from "@core/bigchaindb/transaction";
import keypairService from "@services/keypair.service";
import asset from "@core/bigchaindb/asset";
import env from "@core/env";
import uuidv4 from "uuid/v4";
import axios from "axios";
import { db } from "@models";

function signTx(tx, privateKey) {
    return transaction.sign(tx, privateKey);
}

async function postTx(tx) {
    return await transaction.post(tx);
}

// Just for test
async function createTestBook(publicKey) {
    const bookId = uuidv4();
    const testBook = {
        book_detail: "10",
        book_id: bookId,
        type: "book"
    };

    return transaction.create(testBook, null, publicKey);
}

// Just for test
async function createAndPostTestBook() {
    const bookId = uuidv4();
    const testBook = {
        book_detail: "9",
        book_id: bookId,
        type: "book"
    };

    const txCreated = await transaction.create(testBook, null, env.publicKey);
    const txSigned = await signTx(txCreated, env.privateKey);
    const txPosted = await postTx(txSigned);

    return txPosted;
}

// Just for test
async function transferAndPostTestBook(fraud = false) {
    const txCreate = await createAndPostTestBook();
    const assetId = txCreate.id;
    const transferTx = await transferTestBook(
        assetId,
        "KDeJKo7BhPRCsVwmBjnmuceeFwg1jE6zuLoRnkXy3bL"
    );
    const transferTxSigned = await signTx(transferTx, env.privateKey);

    // Create recept
    const receptTx = await createReceiverConfirmAsset(
        transferTxSigned,
        "KDeJKo7BhPRCsVwmBjnmuceeFwg1jE6zuLoRnkXy3bL"
    );
    const receptTxSigned = await signTx(
        receptTx,
        "DKDeMCSqxPQ6vgqddGPzpWPmVvnP61YkU1v77Wwkm8t9"
    );

    if (!fraud) {
        const txTransferPosted = await postTx(
            receptTxSigned.asset.data.confirm_for_tx
        );
        const txReceptPosted = await postTx(receptTxSigned);

        return {
            transer_tx: txTransferPosted,
            recept_tx: txReceptPosted
        };
    }

    // Fraud here
    const txTransferPosted = await postTx(transferTxSigned);

    return {
        transer_tx: txTransferPosted,
        recept_tx: "This is fraud so it will be null"
    };
}

// Just for test
async function createTestAsset() {
    const { publicKey, privateKey } = keypairService.generateRandomKeyPair();

    const testAsset = {
        type: "test"
    };

    const txCreaed = await transaction.create(testAsset, null, publicKey);
    const txSigned = await signTx(txCreaed, privateKey);
    const txPosted = await postTx(txSigned);

    return txPosted;
}

// Just for test
async function transferTestBook(assetId, publickey) {
    const lastTxs = await asset.getAssetTransactions(assetId);
    const previousTx = lastTxs.length ? lastTxs[lastTxs.length - 1] : null;

    const metadata = {
        transfered: true
    };

    return transaction.transfer(previousTx, publickey, metadata);
}

async function createBookForBookDetailId(bookDetailID) {
    // A UUIDv4 for book instance ID
    const bookId = uuidv4();

    const book = {
        book_detail: bookDetailID.toString(),
        book_id: bookId,
        type: "book"
    };

    // Create new book tx, sign it, and post it to bigchain
    const txCreated = transaction.create(book, null, env.publicKey);
    const txSigned = transaction.sign(txCreated, env.privateKey); // Librarian own this brand new book, so he/she will sign this tx
    const txPosted = await transaction.post(txSigned);

    return txPosted;
}

async function createTransferRequest(assetId, email) {
    // email, public_key here is public key of the receiver
    const publicKey = await asset.getPublicKeyFromEmail(email);

    if (!publicKey) {
        throw new Error("Cannot get publickey of the receiver");
    }

    const lastTxs = await asset.getAssetTransactions(assetId);
    if (!lastTxs || !lastTxs.length) {
        throw new Error("asset_id not available");
    }
    const previousTx = lastTxs.length ? lastTxs[lastTxs.length - 1] : null;

    const metadata = {
        transfer_date: Math.floor(Date.now() / 1000)
    };

    return transaction.transfer(previousTx, publicKey, metadata);
}

async function createReceiverConfirmAsset(transferTxSigned, publicKey) {
    // public_key here is public key of the receiver (confirmer)

    const confirmAsset = {
        confirm_for_tx: transferTxSigned,
        confirm_for_id: transferTxSigned.id,
        confirm_date: Math.floor(Date.now() / 1000),
        type: "recept"
    };

    const receptTx = transaction.create(confirmAsset, null, publicKey);

    // send event to receiver to sign
    const email = await asset.getEmailFromPublicKey(publicKey);

    try {
        await axios.post("https://napi.fptu.tech/api/v1/events/push", {
            email,
            type: "transaction",
            message: receptTx
        });
    } catch (err) {
        throw new Error("Cannot send to receiver");
    }

    return receptTx;
}

async function postToDoneTransfer(confirmAssetSigned) {
    // this is when returner and receiver signed 2 transactons, we will submit it to BigchainDB
    // need to review: need a retry job here? what happen if 1 of 2 request send failed!!?

    const transferTxPosted = await postTx(
        confirmAssetSigned.asset.data.confirm_for_tx
    ); // the confirm asset contains the transfer transaction
    const confirmAssetPosted = await postTx(confirmAssetSigned);

    // Notify for returnerr that things have been done!
    const email = await asset.getEmailFromPublicKey(
        transferTxPosted.inputs[0].owners_before[0]
    );

    // Remove matching from queues when done
    await removeMatchingFromQueueWhenDone(transferTxPosted);

    await axios.post("https://napi.fptu.tech/api/v1/events/push", {
        email,
        type: "success",
        message: "Sách của bạn đã được chuyển thành công"
    });

    return {
        transferTxPosted,
        confirmAssetPosted
    };
}

async function removeMatchingFromQueueWhenDone(transferTxPosted) {
    const bookAssetId = transferTxPosted.asset.id;
    const txs = await asset.getAsset(bookAssetId);
    const bookDetailId = txs.length ? txs[0].asset.data.book_detail : 0;

    if (!bookDetailId) {
        throw new Error("Cannot get bookDetailID");
    }

    const returnerPub = transferTxPosted.inputs[0].owners_before[0];
    const receiverPub = transferTxPosted.outputs[0].public_keys[0];
    const returner = await asset.getEmailFromPublicKey(returnerPub);
    const receiver = await asset.getEmailFromPublicKey(receiverPub);

    const matchingCollection = db.collection("matchings");
    const returnerObj = {
        email: returner,
        bookDetailId: parseInt(bookDetailId),
        bookId: bookAssetId,
        matched: true,
        matchWith: receiver
    };
    const receiverObj = {
        email: receiver,
        bookDetailId: parseInt(bookDetailId),
        matched: true,
        matchWith: returner
    };

    await matchingCollection.deleteOne(returnerObj);
    await matchingCollection.deleteOne(receiverObj);

    return true;
}

export default {
    signTx,
    createTestBook,
    createAndPostTestBook,
    transferAndPostTestBook,
    createTestAsset,
    transferTestBook,
    postTx,
    createBookForBookDetailId,
    createTransferRequest,
    createReceiverConfirmAsset,
    postToDoneTransfer
};