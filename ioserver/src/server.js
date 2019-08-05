import express from "express";
import compression from "compression";
import bodyParser from "body-parser";
import cors from "cors";

import routes from "@routes";
import models from "@models";
import { checkEnvLoaded } from "@core/env";

import initSocketModule from "./socket/socket";
import initRedisModule from "@core/redis";

import {
    sentryMiddleware,
    morganMiddleware
} from "@middlewares/logging.middleware";

import redisAdapter from "socket.io-redis";
const app = express();
export const server = require("http").Server(app);

export const io = require("socket.io")(server);

io.adapter(
    redisAdapter({ host: process.env.REDIS_HOST || "redis", port: 6379 })
);
io.origins("*:*");

async function main(app, server) {
    try {
        checkEnvLoaded();

        // Init DB
        models();

        // Init Sentry logging middleware
        sentryMiddleware();

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
        app.use(morganMiddleware);

        // Init roues
        app.use("/api/v1", routes);

        server.listen(5002, function() {
            console.log("App is listening on port 5002!");
        });

        initSocketModule(server);
        initRedisModule();
    } catch (error) {
        console.error(error);
        process.exit(1);
    }
}

main(app, server);
