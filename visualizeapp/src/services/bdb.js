import * as driver from "bigchaindb-driver";
import bigchain from "../configs/bigchaindb.config";

function getPort(hostname) {
  if (hostname === "fuchain.fptu.tech") {
    return "33565";
  } else {
    return "9984";
  }
}

const hostname = bigchain.host;
const API_PATH = `http://${hostname}:${getPort(hostname)}/api/v1/`;
const conn = new driver.Connection(API_PATH);

export const getTransaction = txId => {
  return conn.getTransaction(txId).then(value => {
    return value;
  });
};

export const getBlock = blockHeight => {
  return conn.getBlock(blockHeight);
};
