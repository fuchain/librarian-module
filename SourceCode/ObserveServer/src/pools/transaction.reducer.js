import {
    ADD_CREATE_TRANSACTION,
    ADD_TRANSFER_TRANSACTION
} from "@constants/action_type.constant";

function addCreateTransaction(transactionPool, action) {
    return {
        createdTxs: [
            ...transactionPool.createdTxs,
            action.payload.createdTransaction
        ],
        transferedTxs: transactionPool.transferedTxs
    };
}

function addTransferTransaction(transactionPool, action) {
    return {
        createdTxs: transactionPool.createdTxs,
        transferedTxs: [
            ...transactionPool.transferedTxs,
            action.payload.createdTransaction
        ]
    };
}

function applyReduce(previousTransactionPool = {}, action) {
    switch (action.type) {
        case ADD_CREATE_TRANSACTION:
            addCreateTransaction(previousTransactionPool, action);
        case ADD_TRANSFER_TRANSACTION:
            addTransferTransaction(previousTransactionPool, action);
    }
}

const reducer = {
    applyReduce
};

export default reducer;
