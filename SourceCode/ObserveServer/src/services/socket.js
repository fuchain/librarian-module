import bigchaindb from "@core/env";
import Logger from "js-logger";
import { client as WebSocketClient } from "websocket";

Logger.useDefaults();
const prefixMessage = "Response from " + bigchaindb.host + ": ";

function onMessage(message) {
    Logger.info(prefixMessage + message.utf8Data);
}

function onError(error) {
    Logger.error(prefixMessage + error);
}

function onClose() {
    Logger.info(prefixMessage + "connection closed.");
}

function setupSocket() {
    const wsClient = new WebSocketClient();

    wsClient.on("connectFailed", onError);
    wsClient.on("connect", connection => {
        Logger.info(prefixMessage + "connection established.");
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
