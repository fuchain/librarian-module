import dotenv from "dotenv";
dotenv.config();

export function checkEnvLoaded() {
    if (
        !process.env.DB_HOST ||
        !process.env.DB_NAME ||
        !process.env.REDIS_HOST ||
        !process.env.BIGCHAIN_HOST ||
        !process.env.PUBLIC_KEY ||
        !process.env.PRIVATE_KEY
    ) {
        console.error(
            "Cannot load local .env file, please ensure that you are using env args (eg: docker-compose)!"
        );
    }
}

export default {
    dbHost: process.env.DB_HOST,
    dbName: process.env.DB_NAME,
    redisHost: process.env.REDIS_HOST,
    bigchainHost: process.env.BIGCHAIN_HOST,
    publicKey: process.env.PUBLIC_KEY,
    privateKey: process.env.PRIVATE_KEY
};
