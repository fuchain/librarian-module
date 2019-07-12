import bigchaindb from "@core/env";
import logger from "noogger";
import { client as WebSocketClient } from "websocket";

const prefixMessage = "Response from " + bigchaindb.host + ": ";

function onMessage(message) {
    const data = JSON.parse(message.utf8Data);
    logger.info(prefixMessage + message.utf8Data);
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

    wsClient.connect(
        bigchaindb.protocol +
            bigchaindb.host +
            bigchaindb.prefix +
            bigchaindb.streamApi
    );
}

export default setupSocket;
