import logger from "noogger";
import bigchainService from "@services/bigchain.service";

function addTransaction(message) {
    if (message.asset_id != message.transaction_id) {
        global.transactionPool = [
            ...global.transactionPool,
            {
                id: message.transaction_id,
                asset_id: message.asset_id
            }
        ];
    }
}

async function validateTransactionPool() {
    logger.info("Validating transaction pool...\n\n");

    // Copy transaction pool to temporary memory
    const transactions = [...global.transactionPool];

    // Clean transaction pool
    global.transactionPool = [];

    transactions.forEach(async transaction => {
        const assets = await bigchainService.searchAssetByTxId(transaction.id);

        if (!assets[0]) {
            if (transaction.retry) {
                logger.error("Transaction exception: " + transaction.id);
            } else {
                // Update back to transaction pool
                global.transactionPool = [
                    ...global.transactionPool,
                    {
                        id: transaction.id,
                        asset_id: transaction.asset_id,
                        retry: true
                    }
                ];
            }
        } else {
            // nothing, skip :)
        }
    });
}

export default {
    addTransaction,
    validateTransactionPool
};
