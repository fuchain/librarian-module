import driver from "fuchain-js";
import env from "@core/env";

const conn = new driver.Connection(env.bigchainHost);

export async function pingBigchainDB() {
    try {
        await conn.pingNode();
        console.log("BigchainDB connected to:", conn.getNodePath());
    } catch (err) {
        throw err;
    }
}

export default conn;
