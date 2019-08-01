const MongoClient = require("mongodb").MongoClient;
import env from "@core/env";

// Connection URL
const url = `mongodb://${env.dbHost}`;

// Database Name
const dbName = `${env.dbName}`;

// Create a new MongoClient
const client = new MongoClient(url, { promiseLibrary: Promise });

// A variable to store the database connection
export let db = null;

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

            // Not close the connect
            // client.close();
        });
    });
}
