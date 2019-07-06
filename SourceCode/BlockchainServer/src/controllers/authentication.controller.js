import keypairService from "@services/keypair.service";

const randomKeypair = async (_, res) => {
    res.send(keypairService.generateRandomKeyPair());
};

async function verifyKeyPairEmail(req, res) {
    const body = req.body;

    try {
        const { token, status } = await keypairService.verifyKeyPairEmail(
            body.token,
            body.public_key
        );
        res.status(201).send({ token, status });
    } catch (err) {
        res.status(422);
        res.send({
            message: err
        });
    }
}

export default { randomKeypair, verifyKeyPairEmail };
