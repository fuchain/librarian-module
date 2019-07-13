// import socket from "@core/socket";
import logger from "noogger";
import streamer from "@handlers/streamer.handler";

function initPool() {
    global.transactionPool = [];
}

function main() {
    try {
        initPool();
        streamer.setupSocket();
    } catch (error) {
        logger.error(error);
        process.exit(1);
    }
}

main();
