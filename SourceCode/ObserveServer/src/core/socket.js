import bigchaindb from "@core/env";
import logger from "noogger";
import { client as WebSocketClient } from "websocket";

const prefixMessage = "Response from " + bigchaindb.host + ": ";

function onError(error) {
    logger.error(prefixMessage + error);
}

function onClose() {
    logger.info(prefixMessage + "connection closed.");
}

function setupSocket(handleData) {
    const wsClient = new WebSocketClient();

    wsClient.on("connectFailed", onError);
    wsClient.on("connect", connection => {
        logger.info(prefixMessage + "connection established.");
        connection.on("message", message => {
            const data = JSON.parse(message.utf8Data);
            handleData(data);
        });
        connection.on("error", onError);
        connection.on("close", onClose);
    });
    const { wsProtocol, wsPort, host, prefix, streamApi } = bigchaindb;
    wsClient.connect(
        `${wsProtocol}://${host}:${wsPort}/${prefix}/${streamApi}`
    );
}

export default { setupSocket };
