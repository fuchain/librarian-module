const driver = require("@core/bigchaindb-driver");

export default function() {
    const newKeyPair = new driver.Ed25519Keypair();

    return newKeyPair;
}
