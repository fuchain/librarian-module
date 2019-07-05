const driver = require("@cores/bigchaindb-driver");

export default function(previousTx, publicKey, metadata) {
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
