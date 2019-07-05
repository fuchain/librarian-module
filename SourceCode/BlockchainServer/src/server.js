import express from "express";
import compression from "compression";
import bodyParser from "body-parser";
import cors from "cors";
import morgan from "morgan";

import routes from "@routes";
import models from "@models";
import { checkEnvLoaded } from "@cores/env";

import initRedisModule from "@cores/redis";

const app = express();
const server = require("http").Server(app);

async function main() {
    try {
        // Check is env is not null
        checkEnvLoaded();

        // Init MongoDB and Redis
        models();
        initRedisModule();

        // Compression gzip
        app.use(compression());

        // Body parseer
        app.use(bodyParser.urlencoded({ extended: false }));
        app.use(bodyParser.json());

        // CORS
        const corsOptions = {
            origin: "*",
            optionsSuccessStatus: 200
        };
        app.use(cors(corsOptions));

        // Middlewares
        app.use(morgan("tiny"));

        // Init routes
        app.use("/api/v1", routes);

        server.listen(5000, function() {
            console.log("App is listening on port 5000!");
        });
    } catch (error) {
        console.error(error);
        process.exit(1);
    }
}

main();
