const driver = require("bigchaindb-driver");
const bip39 = require("bip39");

export default async function(str) {
  const bip39bigcoin = await bip39.mnemonicToSeed(str);
  const bip39bigchain = bip39bigcoin.slice(0, 32);

  return new driver.Ed25519Keypair(bip39bigchain);
}
