import transaction from "@core/bigchaindb/transaction";
import asset from "@core/bigchaindb/asset";

function signTx(tx, privateKey) {
    return transaction.sign(tx, privateKey);
}

async function createTestBook(publickey) {
    const randomBookId = Math.floor(Date.now() / 1000);
    const testBook = {
        book_detail: 1,
        book_id: randomBookId.toString()
    };

    return transaction.create(testBook, null, publickey);
}

async function transferTestBook(assetId, publickey) {
    const lastTxs = await asset.getAssetTransactions(assetId);
    const previousTx = lastTxs.length ? lastTxs[lastTxs.length - 1] : null;

    const metadata = {
        transfered: true
    };

    return transaction.transfer(previousTx, publickey, metadata);
}

async function postTx(tx) {
    try {
        return await transaction.post(tx);
    } catch (err) {
        throw err;
    }
}

export default { signTx, createTestBook, transferTestBook, postTx };
