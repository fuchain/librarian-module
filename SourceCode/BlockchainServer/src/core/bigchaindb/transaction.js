import conn from "@core/bigchaindb";
const driver = require("@core/bigchaindb-driver");

import env from "@core/env";

function create(asset, metadata, publicKey) {
    const txCreate = driver.Transaction.makeCreateTransaction(
        // Define the asset to store
        asset,

        // Metadata contains information about the transaction itself
        // (can be `null` if not needed)
        metadata,

        // A transaction needs an output
        [
            driver.Transaction.makeOutput(
                driver.Transaction.makeEd25519Condition(publicKey)
            )
        ],
        publicKey
    );

    return txCreate;
}

function transfer(previousTx, publicKey, metadata) {
    const txTransfer = driver.Transaction.makeTransferTransaction(
        // signedTx to transfer and output index
        [{ tx: previousTx, output_index: 0 }],

        [
            driver.Transaction.makeOutput(
                driver.Transaction.makeEd25519Condition(publicKey)
            )
        ],

        metadata
    );

    return txTransfer;
}

function sign(tx, privateKey) {
    const txSigned = driver.Transaction.signTransaction(tx, privateKey);

    return txSigned;
}

function post(txSigned) {
    const retrievedTx = conn.postTransactionCommit(txSigned);

    return retrievedTx;
}

async function get(txId) {
    return await conn.getTransaction(txId);
}

function isLibrarianTx(tx) {
    if (
        !tx ||
        !tx.inputs ||
        !tx.inputs.length ||
        !tx.inputs[0].owners_before ||
        !tx.inputs[0].owners_before.length
    )
        return false;

    const owner = tx.inputs[0].owners_before[0];

    return owner === env.publicKey ? true : false;
}

export default { create, transfer, sign, post, get, isLibrarianTx };
