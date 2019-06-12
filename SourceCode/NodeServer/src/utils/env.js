import dotenv from "dotenv";
dotenv.config();

export function checkEnvLoaded() {
    if (
        !process.env.DB_HOST ||
        !process.env.DB_USER ||
        !process.env.DB_PASS ||
        !process.env.DB_NAME ||
        !process.env.DB_DIALECT
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
    dbDialect: process.env.DB_DIALECT
};
