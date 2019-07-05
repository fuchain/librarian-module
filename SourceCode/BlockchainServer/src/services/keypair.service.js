import generateKey from "@cores/bigchaindb/generateKey";

function generateKeyPair() {
    return generateKey();
}

export default { generateKeyPair };
