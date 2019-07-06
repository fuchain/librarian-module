import asset from "@core/bigchaindb/asset";

async function searchBook(id) {
    return await asset.searchAsset(id);
}

export default { searchBook };
