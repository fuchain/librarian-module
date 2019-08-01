import * as localStorage from "@localstorage";
import driver from "fuchain-js";

export default function(tx) {
  const txSigned = driver.Transaction.signTransaction(
    tx,
    localStorage.getItem("privateKey")
  );

  return txSigned;
}
