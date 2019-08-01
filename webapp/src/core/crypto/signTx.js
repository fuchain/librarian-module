import driver from "fuchain-js";
import keypair from "@core/crypto/keypair";

export default function(tx) {
  const privateKey = keypair.get("privateKey");

  const txSigned = driver.Transaction.signTransaction(tx, privateKey);

  return txSigned;
}
