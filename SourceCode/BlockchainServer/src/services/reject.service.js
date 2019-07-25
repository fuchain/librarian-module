import asset from "@core/bigchaindb/asset"

async function getRejectCount(assetId) {
    const transactionList = await asset.searchAsset(assetId);
    const reverseList = transactionList.reverse();
    let rejectCount = 0;
    reverseList.map(transaction => {
        if(transaction.data.type === 'reject'){
            rejectCount++;
        }
    });
    return rejectCount;
}

export default {
    getRejectCount
};
