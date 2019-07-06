import outputService from "@services/output.service";
import asset from "@core/bigchaindb/asset";
import transaction from "@core/bigchaindb/transaction";

async function getProfile(publicKey) {
    const listAssets = await asset.searchPublicKey(publicKey);
    if (listAssets.length) {
        return await asset.getAsset(listAssets[0].id);
    }

    return null;
}

async function getCurrentBook(publicKey) {
    try {
        const transactionIds = await outputService.getUnspent(publicKey);
        const promises = transactionIds.map(e => {
            const transactionId = e.transaction_id;
            const listAssets = asset.getBookFromTransactionId(transactionId);

            return listAssets;
        });

        const result = Promise.all(promises);

        return result;
    } catch (err) {
        throw err;
    }
}

async function getTransferHistory(publicKey) {
    try {
        const txIds = await outputService.getSpent(publicKey);
        const promises = txIds.map(e => {
            const txId = e.transaction_id;
            const tx = transaction.get(txId);

            return tx;
        });

        const result = await Promise.all(promises);

        return result.map(tx => {
            const assetObj = tx.asset.data;
            assetObj.asset_id = tx.id;

            return {
                id: tx.id,
                returner: tx.inputs[0].owners_before[0],
                receiver: tx.outputs[0].public_keys[0],
                operation: tx.operation,
                asset: assetObj
            };
        });
    } catch (err) {
        throw err;
    }
}

export default { getProfile, getCurrentBook, getTransferHistory };
