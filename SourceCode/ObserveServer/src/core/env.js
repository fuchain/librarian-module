import dotenv from "dotenv";
import logger from "noogger";

dotenv.config();

export function checkEnvLoaded() {
    if (
        !process.env.BIGCHAIN_WS_PROTOCOL ||
        !process.env.BIGCHAIN_WS_PORT ||
        !process.env.BIGCHAIN_HTTP_PROTOCOL ||
        !process.env.BIGCHAIN_HOST ||
        !process.env.BIGCHAIN_API_PREFIX ||
        !process.env.BIGCHAIN_STREAM_PATH
    ) {
        logger.error("Cannot load .env file!");
        process.exit(1);
    }
}

export default {
    wsProtocol: process.env.BIGCHAIN_WS_PROTOCOL,
    wsPort: process.env.BIGCHAIN_WS_PORT,
    httpProtocol: process.env.BIGCHAIN_HTTP_PROTOCOL,
    host: process.env.BIGCHAIN_HOST,
    prefix: process.env.BIGCHAIN_API_PREFIX,
    streamApi: process.env.BIGCHAIN_STREAM_PATH
};
