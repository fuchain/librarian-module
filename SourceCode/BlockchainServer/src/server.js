import express from "express";
import compression from "compression";
import bodyParser from "body-parser";
import cors from "cors";
import morgan from "morgan";

import routes from "@routes";
import { initMongoDB } from "@models";
import { initBigchainMongoDB } from "@models/bigchain";
import { checkEnvLoaded } from "@core/env";

import { pingBigchainDB } from "@core/bigchaindb";

const app = express();
const server = require("http").Server(app);

import initQueues from "@queues/";

import { globalErrorHandler } from "@controllers/error.controller";

async function main() {
    try {
        // Init BigchainDB
        pingBigchainDB();

        // Check is env is not null
        checkEnvLoaded();

        // Init MongoDB
        await initMongoDB();

        // Init Bigchain MongoDB
        await initBigchainMongoDB();

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

        // Default page
        app.use("/", function(_, res) {
            res.status(403).send({
                message: "You cannot access this endpoint"
            });
        });

        await initQueues();

        server.listen(5000, function() {
            console.log("App is listening on port 5000!");
        });
    } catch (error) {
        console.error(error);
        process.exit(1);
    }
}

main();
