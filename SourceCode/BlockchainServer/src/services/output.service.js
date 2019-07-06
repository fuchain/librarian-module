import conn from "@core/bigchaindb";

async function getSpent(publicKey) {
    try {
        return await conn.listOutputs(publicKey, true);
    } catch (err) {
        throw err;
    }
}

async function getUnspent(publicKey) {
    try {
        return await conn.listOutputs(publicKey, false);
    } catch (err) {
        throw err;
    }
}

export default { getSpent, getUnspent };