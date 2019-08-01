import express from "express";
import compression from "compression";
import bodyParser from "body-parser";
import routes from "@routes";
import { initMongoDB } from "@models";
import { checkEnvLoaded } from "@core/env";

import { pingBigchainDB } from "@core/fuchain";

const app = express();
const server = require("http").Server(app);

import corsMiddleware from "@middlewares/cors.middleware";
import errorMiddleware from "@middlewares/error.middleware";
import {
    sentryMiddleware,
    morganMiddleware
} from "@middlewares/logging.middleware";

import initWorkers from "@workers";

async function main() {
    try {
        // Init BigchainDB
        pingBigchainDB();

        // Check is env is not null
        checkEnvLoaded();

        // Init MongoDB
        await initMongoDB();

        // Init Sentry logging middleware
        sentryMiddleware();

        // Compression gzip
        app.use(compression());

        // Body parseer
        app.use(bodyParser.urlencoded({ extended: false }));
        app.use(bodyParser.json());

        // CORS Middleware
        app.use(corsMiddleware);

        // Middlewares
        app.use(morganMiddleware);

        // Init routes
        app.use("/api/v1", routes);

        // Error middleware: Handle all error in server
        app.use(errorMiddleware);

        // Default page
        app.use("/", function(_, res) {
            res.status(403).send({
                message: "You cannot access this endpoint"
            });
        });

        // Start workers
        await initWorkers();

        server.listen(5000, function() {
            console.log("App is listening on port 5000!");
        });
    } catch (error) {
        console.error(error);
        process.exit(1);
    }
}

main();
