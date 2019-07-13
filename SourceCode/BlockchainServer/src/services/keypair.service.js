import generateKey from "@core/bigchaindb/generateKey";
import transaction from "@core/bigchaindb/transaction";
import asset from "@core/bigchaindb/asset";
import env from "@core/env";
import axios from "axios";
import { createJWT } from "@core/jwt";
import { db } from "@models";

function generateRandomKeyPair() {
    return generateKey();
}

async function generateKeyPairEmail(email, publicKey, fullname) {
    const asset = {
        email: email.toLowerCase(),
        type: "reader"
    };

    const metadata = {
        public_key: publicKey
    };

    // Submit transaction
    const tx = transaction.create(asset, metadata, env.publicKey);
    const txSigned = transaction.sign(tx, env.privateKey);
    const txDone = await transaction.post(txSigned);

    // Create user row in DB
    const userCollection = db.collection("users");
    await userCollection.insertMany([
        {
            email,
            fullname,
            phone: null
        }
    ]);

    return txDone;
}

async function checkTokenGoogle(token) {
    const googleAuth = "https://www.googleapis.com/userinfo/v2/me";
    const axiosConfig = {
        headers: {
            Authorization: "Bearer " + token
        }
    };

    const { data } = await axios.get(googleAuth, axiosConfig);

    const email = data.email;
    const name = data.name;
    const picture = data.picture;

    return { email, name, picture };
}

async function isEmailExisted(email) {
    const arr = await asset.searchEmail(email);

    const found = arr.find(e => e.data.email === email);

    return found ? true : false;
}

async function verifyKeyPairEmail(token, publicKey) {
    const { email, name, picture } = await checkTokenGoogle(token);
    const found = await isEmailExisted(email);

    if (!found) {
        const tx = await generateKeyPairEmail(email, publicKey, name);
        return tx
            ? { token: createJWT(email), status: "created" }
            : { token: createJWT(email), status: "verified" };
    } else {
        return {
            token: createJWT(email),
            status: "verified",
            email,
            name,
            picture
        };
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
