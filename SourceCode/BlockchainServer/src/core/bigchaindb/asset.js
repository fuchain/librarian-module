import conn from "@core/bigchaindb";

async function getAsset(id) {
    try {
        return await conn.getAsset(id);
    } catch (err) {
        throw err;
    }
}

async function getAssetTransactions(assetId) {
    return await conn.listTransactions(assetId);
}

async function searchAsset(bookId) {
    return await conn.searchAssets(bookId);
}

async function searchEmail(email) {
    return await conn.searchAssets(email);
}

async function searchPublicKey(pubickey) {
    return await conn.searchMetadata(pubickey);
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
    getBookFromTransactionId
};
