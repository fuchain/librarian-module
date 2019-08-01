import asset from "@core/fuchain/asset";

async function getRejectCount(assetId) {
    const transactionList = await asset.searchAsset(assetId);
    const reverseList = transactionList.reverse();

    let rejectCount = 0;
    for (const asset of reverseList) {
        if (asset.data.type !== "reject") {
            break;
        }

        rejectCount++;
    }

    return rejectCount;
}

export default {
    getRejectCount
};
