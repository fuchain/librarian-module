import generateKey from "@core/bigchaindb/generateKey";
import transaction from "@core/bigchaindb/transaction";
import env from "@core/env";
import axios from "axios";

function generateRandomKeyPair() {
    return generateKey();
}

async function generateKeyPairEmail(token, publicKey) {
    try {
        const googleAuth = "https://www.googleapis.com/userinfo/v2/me";
        const axiosConfig = {
            headers: {
                Authorization: "bearer " + token
            }
        };

        const { email } = await axios.get(googleAuth, axiosConfig);

        const asset = {
            email,
            type: "reader"
        };

        const metadata = {
            public_key: publicKey
        };

        const tx = transaction.create(asset, metadata, env.publickey);
        const txSigned = transaction.sign(tx, env.privateKey);

        const txDone = await transaction.post(txSigned);
        return txDone;
    } catch (err) {
        throw err;
    }
}

export default { generateRandomKeyPair, generateKeyPairEmail };
