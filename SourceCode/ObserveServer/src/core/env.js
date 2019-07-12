import dotenv from "dotenv";
import Logger from "js-logger";

dotenv.config();
Logger.useDefaults();

export function checkEnvLoaded() {
    if (
        !process.env.BIGCHAIN_PROTOCOL ||
        !process.env.BIGCHAIN_HOST ||
        !process.env.BIGCHAIN_API_PREFIX ||
        !process.env.BIGCHAIN_STREAM_PATH
    ) {
        Logger.error("Cannot load .env file!");
        process.exit(1);
    }
}

export default {
    protocol: process.env.BIGCHAIN_PROTOCOL,
    host: process.env.BIGCHAIN_HOST,
    prefix: process.env.BIGCHAIN_API_PREFIX,
    streamApi: process.env.BIGCHAIN_STREAM_PATH
};