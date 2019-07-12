const createTx = [];
const transferTx = [];

function createTransactionPool() {
    return {
        createdTxs: [],
        transferedTxs: []
    };
}

const store = { createTransactionPool };

export default store;
