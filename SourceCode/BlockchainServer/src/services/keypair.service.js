import generateKey from "@core/bigchaindb/generateKey";
import transaction from "@core/bigchaindb/transaction";
import asset from "@core/bigchaindb/asset";
import env from "@core/env";
import axios from "axios";
import { createJWT } from "@core/jwt";

function generateRandomKeyPair() {
    return generateKey();
}

async function generateKeyPairEmail(email, publicKey) {
    try {
        const asset = {
            email,
            type: "reader"
        };

        const metadata = {
            public_key: publicKey
        };

        const tx = transaction.create(asset, metadata, env.publicKey);
        const txSigned = transaction.sign(tx, env.privateKey);

        const txDone = await transaction.post(txSigned);
        return txDone;
    } catch (err) {
        throw err;
    }
}

async function checkTokenGoogle(token) {
    const googleAuth = "https://www.googleapis.com/userinfo/v2/me";
    const axiosConfig = {
        headers: {
            Authorization: "Bearer " + token
        }
    };

    const response = await axios.get(googleAuth, axiosConfig);
    const email = response.data.email;

    return { email };
}

async function isEmailExisted(email) {
    const arr = await asset.searchEmail(email);

    const found = arr.find(e => e.data.email === email);

    return found ? true : false;
}

async function verifyKeyPairEmail(token, publicKey) {
    try {
        const { email } = await checkTokenGoogle(token);
        const found = await isEmailExisted(email);

        if (!found) {
            const tx = await generateKeyPairEmail(email, publicKey);
            return tx
                ? { token: createJWT(email), status: "created" }
                : { token: createJWT(email), status: "verified" };
        } else {
            return { token: createJWT(email), status: "verified" };
        }
    } catch (err) {
        throw err;
    }
}

async function checkExistedEmailFromToken(token) {
    const { email } = await checkTokenGoogle(token);

    return isEmailExisted(email);
}

export default {
    generateRandomKeyPair,
    verifyKeyPairEmail,
    checkExistedEmailFromToken
};
