import asset from "@core/bigchaindb/asset"

async function getRejectCount(assetId) {
    const transactionList = await asset.searchAsset(assetId);
    const reverseList = transactionList.reverse();
    let rejectCount = 0;
    for (let i = 0; i < reverseList.length; i++) {
        if (reverseList[i].data.type !== 'reject') {
            break;
        }
        rejectCount++;
    }
    return rejectCount;
}

export default {
    getRejectCount
};
