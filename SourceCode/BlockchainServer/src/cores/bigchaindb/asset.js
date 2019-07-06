import conn from "@cores/bigchaindb";

async function getAssetTransactions(assetId) {
    return await conn.listTransactions(assetId);
}

export default { getAssetTransactions };
