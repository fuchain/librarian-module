import bigchaindb from "@core/env";
import logger from "noogger";
import { client as WebSocketClient } from "websocket";

const prefixMessage = "Response from " + bigchaindb.host + ": ";

function onMessage(message) {
    const data = JSON.parse(message.utf8Data);
    logger.info(prefixMessage + message.utf8Data);
    console.log("height: " + data.height);
    console.log("asset_id: " + data.asset_id);
    console.log("transaction_id: " + data.transaction_id);
    console.log("logger: " + logger.module);
}

function onError(error) {
    logger.error(prefixMessage + error);
}

function onClose() {
    logger.info(prefixMessage + "connection closed.");
}

function setupSocket() {
    const wsClient = new WebSocketClient();

    wsClient.on("connectFailed", onError);
    wsClient.on("connect", connection => {
        logger.info(prefixMessage + "connection established.");
        connection.on("message", onMessage);
        connection.on("error", onError);
        connection.on("close", onClose);
    });
    const { wsProtocol, wsPort, host, prefix, streamApi } = bigchaindb;
    wsClient.connect(
        `${wsProtocol}://${host}:${wsPort}/${prefix}/${streamApi}`
    );
}

export default { setupSocket };
