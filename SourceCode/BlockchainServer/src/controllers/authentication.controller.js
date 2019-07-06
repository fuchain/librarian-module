import keypairService from "@services/keypair.service";

const newKeypair = async (_, res) => {
    res.send(keypairService.generateRandomKeyPair());
};

const newKeyPairEmail = async (req, res) => {
    const body = req.body;

    try {
        await keypairService.generateKeyPairEmail(body.token, body.public_key);
        res.status(201).send();
    } catch (err) {
        res.status(422);
        res.send({
            message: err
        });
    }
};

export default { newKeypair, newKeyPairEmail };
