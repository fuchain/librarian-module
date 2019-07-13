import bigchaindb from "@core/env";
import logger from "noogger";
import { client as WebSocketClient } from "websocket";
import poolService from "@services/pool.service";

const prefixMessage = "Response from " + bigchaindb.host + ": ";

function onMessage(message) {
    const data = message.utf8Data;
    logger.info(prefixMessage + data);
    poolService.addTransaction(data);
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

function start() {
    setupSocket();
}

export default { start };
