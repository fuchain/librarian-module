import conn from "@core/bigchaindb";

async function getAssetTransactions(assetId) {
    return await conn.listTransactions(assetId);
}

async function searchAsset(bookId) {
    return await conn.searchAssets(bookId);
}

async function searchEmail(email) {
    return await conn.searchAssets(email);
}

export default { getAssetTransactions, searchAsset, searchEmail };
