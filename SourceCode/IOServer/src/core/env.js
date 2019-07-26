import dotenv from "dotenv";
dotenv.config();

export function checkEnvLoaded() {
    if (
        !process.env.DB_HOST ||
        !process.env.DB_NAME ||
        !process.env.REDIS_HOST ||
        !process.env.SENDGRID_API_KEY
    ) {
        console.error("Cannot load .env file!");
    }
}

export default {
    dbHost: process.env.DB_HOST,
    dbName: process.env.DB_NAME,
    redisHost: process.env.REDIS_HOST,
    sendgridKey: process.env.SENDGRID_API_KEY
};
