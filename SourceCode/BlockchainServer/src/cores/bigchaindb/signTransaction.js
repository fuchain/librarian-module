const driver = require("@cores/bigchaindb-driver");

export default function(tx, privateKey) {
    const txSigned = driver.Transaction.signTransaction(tx, privateKey);

    return txSigned;
}
