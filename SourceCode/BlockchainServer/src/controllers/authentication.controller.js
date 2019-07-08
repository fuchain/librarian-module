import errorHandler from "@core/handlers/error.handler";
import keypairService from "@services/keypair.service";

const randomKeypair = async (_, res) => {
    res.send(keypairService.generateRandomKeyPair());
};

async function verifyKeyPairEmail(req, res) {
    const { token, status } = await keypairService.verifyKeyPairEmail(
        body.token,
        body.public_key
    );
    res.status(201).send({ token, status });
    res.status(422);
    res.send({
        message: err instanceof Error ? err.toString() : err
    });
}

export default errorHandler({ randomKeypair, verifyKeyPairEmail });
