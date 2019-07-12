import setupSocket from "@services/socket";
import logger from "noogger";

function main() {
    try {
        setupSocket();
    } catch (error) {
        logger.error(error);
        process.exit(1);
    }
}

main();
