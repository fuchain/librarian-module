const driver = require("@cores/bigchaindb-driver");

export default function(asset, metadata, publicKey) {
    const tx = driver.Transaction.makeCreateTransaction(
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

    return tx;
}
