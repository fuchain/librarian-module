const bip39 = require("bip39");

export default function generateSeed() {
  return bip39.generateMnemonic();
}
