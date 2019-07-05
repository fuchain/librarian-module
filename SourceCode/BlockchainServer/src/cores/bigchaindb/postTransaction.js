import conn from "@cores/bigchaindb";

export default async function(txSigned) {
    try {
        const retrievedTx = conn.postTransactionCommit(txSigned);

        return retrievedTx;
    } catch (err) {
        throw err;
    }
}
