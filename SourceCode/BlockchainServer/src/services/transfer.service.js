import transaction from "@core/bigchaindb/transaction";
import keypairService from "@services/keypair.service";
import asset from "@core/bigchaindb/asset";
import env from "@core/env";
import uuidv4 from "uuid/v4";

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
        book_detail: "7",
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

    // Creaate recept
    const receptTx = createReceiverConfirmAsset(
        transferTxSigned,
        "KDeJKo7BhPRCsVwmBjnmuceeFwg1jE6zuLoRnkXy3bL"
    );
    const receptTxSigned = await signTx(
        receptTx,
        "DKDeMCSqxPQ6vgqddGPzpWPmVvnP61YkU1v77Wwkm8t9"
    );

    if (!fraud) {
        const txTransferPosted = await postTx(transferTxSigned);
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

async function createTransferRequest(assetId, publicKey) {
    // public_key here is public key of the receiver

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

function createReceiverConfirmAsset(transferTxSigned, publicKey) {
    // public_key here is public key of the receiver (confirmer)

    const confirmAsset = {
        confirm_for_tx: transferTxSigned,
        confirm_for_id: transferTxSigned.id,
        confirm_date: Math.floor(Date.now() / 1000),
        type: "recept"
    };

    return transaction.create(confirmAsset, null, publicKey);
}

async function postToDoneTransfer(confirmAssetSigned) {
    // this is when returner and receiver signed 2 transactons, we will submit it to BigchainDB
    // need to review: need a retry job here? what happen if 1 of 2 request send failed!!?

    const transferTxPosted = await postTx(
        confirmAssetSigned.metadata.confirm_for
    ); // the confirm asset contains the transfer transaction
    const confirmAssetPosted = await postTx(confirmAssetSigned);

    return {
        transferTxPosted,
        confirmAssetPosted
    };
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
