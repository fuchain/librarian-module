import mongoose from "mongoose";
import env from "@cores/env";

const conn = mongoose.createConnection(
    `mongodb://${env.dbHost}:27017/${env.dbName}`,
    {
        auth: { authSource: "admin" },
        user: env.dbUser,
        pass: env.dbPass
    }
);

function init() {
    conn.on("error", console.error.bind(console, "MongoDB connection error:"));
    conn.once("open", function() {
        console.log("MongoDB connected to:", env.dbHost);
    });
}

// export const ModelA = conn.model("ModelA", modelASchema);

export default init;
