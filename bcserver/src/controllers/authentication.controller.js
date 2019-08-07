import errorHandler from "@core/handlers/error.handler";
import keypairLogic from "@logics/keypair.logic";

async function randomKeypair(_, res) {
    res.send(keypairLogic.generateRandomKeyPair());
}

async function verifyKeyPairEmail(req, res) {
    const body = req.body;

    const {
        token,
        status,
        name,
        picture
    } = await keypairLogic.verifyKeyPairEmail(body.token, body.public_key);
    res.status(201).send({ token, status, name, picture });
}

export default errorHandler({ randomKeypair, verifyKeyPairEmail });
