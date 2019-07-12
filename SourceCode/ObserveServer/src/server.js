import setupSocket from "@services/socket";
import logger from "noogger";
import transactionPool from "./pools";

function main() {
    try {
        setupSocket();
    } catch (error) {
        logger.error(error);
        process.exit(1);
    }
}

main();
