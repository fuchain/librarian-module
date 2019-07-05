const driver = require("@cores/bigchaindb-driver");
import env from "@cores/env";

const conn = new driver.Connection(env.bigchainHost);

export default conn;
