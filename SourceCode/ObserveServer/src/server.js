import logger from "noogger";
import streamer from "@handlers/streamer.handler";
import scheduler from "@handlers/scheduler.handler";

function initPool() {
    global.transactionPool = [];
}

function main() {
    try {
        initPool();
        streamer.start();
        scheduler.start();
    } catch (error) {
        logger.error(error);
        process.exit(1);
    }
}

main();
