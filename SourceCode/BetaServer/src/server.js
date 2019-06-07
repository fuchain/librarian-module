import express from "express";
import compression from "compression";
import bodyParser from "body-parser";
import cors from "cors";
import morgan from "morgan";

import routes from "@routes";
import models from "@models";
import { checkEnvLoaded } from "@utils/env";

const app = express();

async function main() {
    try {
        checkEnvLoaded();

        // Init DB
        try {
            models.sequelize.authenticate();
            models.sequelize.sync({ force: true });
        } catch (dbError) {
            console.error("DB Error: ", dbError);
            process.exit(1);
        }

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

        // Init roues
        app.use("/api/v1", routes);

        app.listen(3000, function() {
            console.log("App is listening on port 3000!");
        });
    } catch (error) {
        console.error(error);
        process.exit(1);
    }
}

main();
