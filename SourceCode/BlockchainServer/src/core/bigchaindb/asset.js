import conn from "@core/bigchaindb";
import transaction from "@core/bigchaindb/transaction";

async function getAsset(id) {
    return await conn.getAsset(id);
}

async function getAssetTransactions(assetId) {
    return await conn.listTransactions(assetId);
}

async function searchAsset(searchText) {
    return await conn.searchAssets(searchText);
}

async function searchEmail(email) {
    return await conn.searchAssets(email);
}

async function searchPublicKey(pubickey) {
    return await conn.searchMetadata(pubickey);
}

async function getEmailFromPublicKey(publicKey) {
    const listTx = await conn.searchMetadata(publicKey);

    if (!listTx || !listTx.length) throw new Error("Transaction not valid");

    const txId = listTx[0].id;
    const tx = await transaction.get(txId);

    if (transaction.isLibrarianTx(tx)) {
        const asset = tx.asset.data;
        const email = asset.email;

        return email;
    } else {
        throw new Error("Transaction not sign by librarian");
    }
}

async function getPublicKeyFromEmail(email) {
    const listTx = await conn.searchAssets(email);

    if (!listTx || !listTx.length) throw new Error("Transaction not valid");

    const txId = listTx[0].id;
    const tx = await transaction.get(txId);

    if (transaction.isLibrarianTx(tx)) {
        return tx.metadata.public_key;
    } else {
        throw new Error("Transaction not sign by librarian");
    }
}

async function getBookFromTransactionId(transactionId) {
    const transaction = await conn.getTransaction(transactionId);

    const assetId =
        transaction.operation === "TRANSFER"
            ? transaction.asset.id
            : transaction.id;

    if (!assetId) {
        return null;
    }

    const assets = await conn.getAsset(assetId);

    if (assets.length) {
        const asset = assets[0].asset.data;
        asset.asset_id = assets[0].id;

        return asset;
    }

    return null;
}

export default {
    getAsset,
    getAssetTransactions,
    searchAsset,
    searchEmail,
    searchPublicKey,
    getEmailFromPublicKey,
    getPublicKeyFromEmail,
    getBookFromTransactionId
};
