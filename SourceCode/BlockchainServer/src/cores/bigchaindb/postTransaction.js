import conn from "@cores/bigchaindb";

export default async function() {
    try {
        const retrievedTx = conn.postTransactionCommit(txSigned);

        return retrievedTx;
    } catch (err) {
        throw err;
    }
}
