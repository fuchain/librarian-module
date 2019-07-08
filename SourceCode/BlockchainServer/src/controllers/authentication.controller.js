import errorHandler from "@core/handlers/error.handler";
import keypairService from "@services/keypair.service";

const randomKeypair = async (_, res) => {
    res.send(keypairService.generateRandomKeyPair());
};

async function verifyKeyPairEmail(req, res) {
    const body = req.body;

    const { token, status } = await keypairService.verifyKeyPairEmail(
        body.token,
        body.public_key
    );
    res.status(201).send({ token, status });
}

export default errorHandler({ randomKeypair, verifyKeyPairEmail });
