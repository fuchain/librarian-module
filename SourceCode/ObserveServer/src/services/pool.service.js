function addTransaction(transaction) {
    global.transactionPool.push(transaction);
}

function validateTransactionPool() {
    const transactions = [...global.transactionPool];
    transactions.map(transaction => {
        // query to bigchain to check with CREATE tx
    });
}

export default {
    addTransaction,
    validateTransactionPool
};
