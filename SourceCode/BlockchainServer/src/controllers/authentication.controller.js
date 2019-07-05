import keypairService from "@services/keypair.service";

const newKeypair = async (_, res) => {
    res.send(keypairService.generateKeyPair());
};

export default { newKeypair };
