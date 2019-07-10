const MongoClient = require("mongodb").MongoClient;
import env from "@core/env";

// Connection URL
const url = `mongodb://${env.dbHost}:27017`;

// Database Name
const dbName = `${env.dbName}`;

// Create a new MongoClient
const client = new MongoClient(url, { promiseLibrary: Promise });

export let db;

export async function initMongoDB() {
    // Use connect method to connect to the Server
    return new Promise(function(resolve, reject) {
        client.connect(function(err) {
            if (err) {
                reject(new Error("Cannot connect to MongoDB:", env.dbHost));
            }

            console.log("MongoDB connected to:", env.dbHost);
            db = client.db(dbName);

            resolve();

            // client.close();
        });
    });
}
