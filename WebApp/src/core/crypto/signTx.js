import * as localStorage from "@localstorage";
const driver = require("bigchaindb-driver");

export default function(tx) {
  const txSigned = driver.Transaction.signTransaction(
    tx,
    localStorage.getItem("privateKey")
  );

  return txSigned;
}
