import setupSocket from "@services/socket";
import Logger from "js-logger";

Logger.useDefaults();

function main() {
    try {
        setupSocket();
    } catch (error) {
        Logger.error(error);
        process.exit(1);
    }
}

main();
