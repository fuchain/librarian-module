import conn from "@core/fuchain";
import transaction from "@core/fuchain/transaction";
import env from "core/env";

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
    if (publicKey === env.publicKey) {
        return "librarian@fptu.tech";
    }

    const listTx = await conn.searchMetadata(publicKey);

    if (!listTx || !listTx.length) throw new Error("Invalid public key");

    const txId = listTx[0].id;
    const tx = await transaction.get(txId);

    if (!transaction.isLibrarianTx(tx)) {
        throw new Error("Keypair not sign by librarian");
    }

    const assetId = tx.operation === "CREATE" ? tx.id : tx.asset.id;
    const txs = await conn.getAsset(assetId);

    if (!txs || !txs.length) {
        throw new Error("Invalid publicKey");
    }

    const lastTxIndex = txs.length - 1;
    const validTx = txs[lastTxIndex];
    if (publicKey !== validTx.metadata.public_key) {
        throw new Error("Public key is not valid");
    }

    const email = txs[0].asset.data.email;

    return email;
}

async function getPublicKeyFromEmail(email) {
    // Lower it first
    email = email.toLowerCase();

    const listTx = await conn.searchAssets(email);

    if (!listTx || !listTx.length) throw new Error("Transaction not valid");

    if (listTx[0].data.email !== email) throw new Error("Email not found");

    const assetId = listTx[0].id;
    const txs = await conn.getAsset(assetId);

    if (!txs || !txs.length) throw new Error("Email not valid");

    const tx = txs[txs.length - 1];

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

async function getAllUsers(type) {
    const users = await conn.searchAssets(type);

    return users;
}

export default {
    getAsset,
    getAssetTransactions,
    searchAsset,
    searchEmail,
    searchPublicKey,
    getEmailFromPublicKey,
    getPublicKeyFromEmail,
    getBookFromTransactionId,
    getAllUsers
};
