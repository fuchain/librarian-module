import {
    ADD_CREATE_TRANSACTION,
    ADD_TRANSFER_TRANSACTION
} from "./transaction.constant";

function addCreateTransaction(createdTransaction) {
    return {
        type: ADD_CREATE_TRANSACTION,
        payload: { createdTransaction }
    };
}

function addTransferTransaction(transferedTransaction) {
    return {
        type: ADD_TRANSFER_TRANSACTION,
        payload: { transferedTransaction }
    };
}

const action = {
    addCreateTransaction,
    addTransferTransaction
};

export default action;
