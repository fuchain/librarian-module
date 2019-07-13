import logger from "noogger";

function addTransaction(transaction) {
    global.transactionPool.push(transaction);
}

function validateTransactionPool() {
    logger.info("Validating transaction pool....")
    const transactions = [...global.transactionPool];
    global.transactionPool = [];
    transactions.map(transaction => {
        // query to bigchain to check with CREATE tx
    });
    // check tmp pool is empty or not
    // send exception to log service of tmp pool is not empty
}

export default {
    addTransaction,
    validateTransactionPool
};
