import express from "express";
import compression from "compression";
import bodyParser from "body-parser";
import cors from "cors";
import morgan from "morgan";

import routes from "@routes";
import { initMongoDB } from "@models";
import { checkEnvLoaded } from "@core/env";

import initRedisModule from "@core/redis";
import { pingBigchainDB } from "@core/bigchaindb";

const app = express();
const server = require("http").Server(app);

import importDBQueue from "@queues/importdb.queue";
import matchingQueue from "@queues/matching.queue";
import pairQueue from "@queues/pair.queue";
import pairUpdateQueue from "@queues/pair.update.queue";

import { globalErrorHandler } from "@controllers/error.controller";

async function main() {
    try {
        // Init BigchainDB
        pingBigchainDB();

        // Check is env is not null
        checkEnvLoaded();

        // Init MongoDB and Redis
        await initMongoDB();
        await initRedisModule();

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

        // Global error handler
        app.use(globalErrorHandler);

        // Queues
        await matchingQueue.run();
        await importDBQueue.run();
        await pairQueue.run();
        await pairQueue.addJob();
        await pairUpdateQueue.run();

        server.listen(5000, function() {
            console.log("App is listening on port 5000!");
        });
    } catch (error) {
        console.error(error);
        process.exit(1);
    }
}

main();
