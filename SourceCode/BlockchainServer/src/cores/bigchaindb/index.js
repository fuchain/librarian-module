const driver = require("@cores/bigchaindb-driver");
import env from "@cores/env";

const conn = new driver.Connection(env.bigchainHost);

export async function pingBigchainDB() {
    try {
        await conn.pingNode();
        console.log("Connected to:", conn.getNodePath());
    } catch (err) {
        throw err;
    }
}

export default conn;
