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

async function getRejectTransactionOfABook(assetId) {
    const rejectAssets = await asset.searchAsset("reject");
    const rejectList = rejectAssets.filter(
        e => e.data.confirm_for_tx.asset.id === assetId
    );

    const promises = rejectList.map(async e => {
        const returner = await asset.getEmailFromPublicKey(
            e.data.confirm_for_tx.inputs[0].owners_before[0]
        );
        const receiver = await asset.getEmailFromPublicKey(
            e.data.confirm_for_tx.outputs[0].public_keys[0]
        );

        return {
            id: e.id,
            returner,
            receiver,
            transfer_date: e.data.confirm_date,
            type: "reject"
        };
    });

    return await Promise.all(promises);
}

export default {
    getRejectCount,
    getRejectTransactionOfABook
};
