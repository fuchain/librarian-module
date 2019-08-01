import driver from "fuchain-js";

export default function() {
    const newKeyPair = new driver.Ed25519Keypair();

    return newKeyPair;
}
