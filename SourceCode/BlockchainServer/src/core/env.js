import dotenv from "dotenv";
dotenv.config();

export function checkEnvLoaded() {
    if (
        !process.env.DB_HOST ||
        !process.env.DB_USER ||
        !process.env.DB_PASS ||
        !process.env.DB_NAME ||
        !process.env.REDIS_HOST ||
        !process.env.BIGCHAIN_HOST ||
        !process.env.PUBLIC_KEY ||
        !process.env.PRIVATE_KEY
    ) {
        console.error("Cannot load .env file!");
        process.exit(1);
    }
}

export default {
    dbHost: process.env.DB_HOST,
    dbUser: process.env.DB_USER,
    dbPass: process.env.DB_PASS,
    dbName: process.env.DB_NAME,
    redisHost: process.env.REDIS_HOST,
    bigchainHost: process.env.BIGCHAIN_HOST,
    publicKey: process.env.PUBLIC_KEY,
    privateKey: process.env.PRIVATE_KEY
};
