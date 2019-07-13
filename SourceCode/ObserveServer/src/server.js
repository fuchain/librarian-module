import socket from "@core/socket";
import logger from "noogger";

function main() {
    try {
        socket.setupSocket();
    } catch (error) {
        logger.error(error);
        process.exit(1);
    }
}

main();
