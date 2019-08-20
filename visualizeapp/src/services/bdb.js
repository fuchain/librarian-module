import * as driver from "bigchaindb-driver";
import bigchain from "../configs/bigchaindb.config";

const hostname = bigchain.host;
const API_PATH = `http://${hostname}:33565/api/v1/`;
const conn = new driver.Connection(API_PATH);

export const getTransaction = txId => {
  return conn.getTransaction(txId).then(value => {
    return value;
  });
};

export const getBlock = blockHeight => {
  return conn.getBlock(blockHeight);
};
