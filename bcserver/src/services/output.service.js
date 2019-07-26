import conn from "@core/bigchaindb";

async function getSpent(publicKey) {
    return await conn.listOutputs(publicKey, true);
}

async function getUnspent(publicKey) {
    return await conn.listOutputs(publicKey, false);
}

export default { getSpent, getUnspent };
