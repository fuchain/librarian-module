import generateKey from "@core/fuchain/generateKey";
import transaction from "@core/fuchain/transaction";
import asset from "@core/fuchain/asset";
import env from "@core/env";
import axios from "axios";
import { createJWT } from "@core/jwt";
import { db } from "@core/db";
import uploadLogic from "@logics/upload.logic";
import constants from "@core/constants";

function generateRandomKeyPair() {
    return generateKey();
}

async function createNewUserInDB(email, fullname = null) {
    const usersCollection = db.collection("users");

    const user = await usersCollection.findOne({ email });
    if (user) {
        return;
    }

    // Create user row in DB
    await usersCollection.insertMany([
        {
            email,
            fullname,
            phone: null
        }
    ]);
}

async function generateKeyPairEmail(email, publicKey, fullname) {
    // Lower case
    email = email.toLowerCase();

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

    await createNewUserInDB(email, fullname);

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
    const pictureUrl = data.picture;

    try {
        // Save to cloud
        await uploadLogic.saveUserAvatar(pictureUrl, email);

        const response = await axios.get(pictureUrl, {
            responseType: "arraybuffer"
        });
        const picture = new Buffer(response.data, "binary").toString("base64");

        return { email, name, picture };
    } catch (err) {
        console.log(err);

        const picture = null;
        return { email, name, picture };
    }
}

async function isEmailExisted(email) {
    // Lower case
    email = email.toLowerCase();

    const arr = await asset.searchEmail(email);

    const found = arr.find(e => e.data.email === email);

    return found ? true : false;
}

async function verifyKeyPairEmail(token, publicKey) {
    const data = await checkTokenGoogle(token);
    const { name, picture } = data;
    const email = data.email.toLowerCase();

    await createNewUserInDB(email, name);

    // Check if it is librarian
    if (publicKey === env.publicKey) {
        if (email === constants.LIBRARIAN_EMAIL) {
            return {
                token: createJWT(email, true),
                status: "verified",
                email,
                name,
                picture
            };
        } else {
            throw new Error("Wrong authentication for librarian");
        }
    }

    const found = await isEmailExisted(email);

    if (!found) {
        const tx = await generateKeyPairEmail(email, publicKey, name);
        return tx
            ? {
                  token: createJWT(email),
                  status: "created",
                  email,
                  name,
                  picture
              }
            : {
                  token: createJWT(email),
                  status: "verified",
                  email,
                  name,
                  picture
              };
    } else {
        const emailWallet = await asset.getEmailFromPublicKey(publicKey);
        if (email !== emailWallet) {
            throw new Error("Keypair and email are not matched");
        }

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
