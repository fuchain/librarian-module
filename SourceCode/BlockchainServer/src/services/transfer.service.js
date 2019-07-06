import transaction from "@core/bigchaindb/transaction";
import asset from "@core/bigchaindb/asset";

function signTx(tx, privateKey) {
    return transaction.sign(tx, privateKey);
}

async function postTx(tx) {
    try {
        return await transaction.post(tx);
    } catch (err) {
        throw err;
    }
}

// Just for test
async function createTestBook(publickey) {
    const randomBookId = Math.floor(Date.now() / 1000);
    const testBook = {
        book_detail: "bd_1",
        book_id: randomBookId.toString(),
        type: "book"
    };

    return transaction.create(testBook, null, publickey);
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

async function createTransferRequest(assetId, publicKey) {
    // public_key here is public key of the receiver

    try {
        const lastTxs = await asset.getAssetTransactions(assetId);
        const previousTx = lastTxs.length ? lastTxs[lastTxs.length - 1] : null;

        const metadata = {
            transfer_date: Math.floor(Date.now() / 1000)
        };

        return transaction.transfer(previousTx, publicKey, metadata);
    } catch (err) {
        throw err;
    }
}

function createReceiverConfirmAsset(transferTxSigned, publicKey) {
    // public_key here is public key of the receiver (confirmer)

    const confirmAsset = {
        transaction_id: transferTxSigned.id,
        confirm_date: Math.floor(Date.now() / 1000)
    };

    const metadata = {
        confirm_for: transferTx
    };

    return transaction.create(confirmAsset, metadata, publicKey);
}

async function postToDoneTransfer(confirmAssetSigned) {
    // this is when returner and receiver signed 2 transactons, we will submit it to BigchainDB
    // need to review: need a retry job here? what happen if 1 of 2 request send failed!!?

    try {
        const transferTxPosted = await postTx(
            confirmAssetSigned.metadata.confirm_for
        ); // the confirm asset contains the transfer transaction
        const confirmAssetPosted = await postTx(confirmAssetSigned);

        return {
            transferTxPosted,
            confirmAssetPosted
        };
    } catch (err) {
        throw err;
    }
}

export default {
    signTx,
    createTestBook,
    transferTestBook,
    postTx,
    createTransferRequest,
    createReceiverConfirmAsset,
    postToDoneTransfer
};
