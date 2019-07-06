import conn from "@core/bigchaindb";

async function getAssetTransactions(assetId) {
    return await conn.listTransactions(assetId);
}

async function searchAsset(bookId) {
    return await conn.searchAssets(bookId);
}

export default { getAssetTransactions, searchAsset };
