const MongoClient = require("mongodb").MongoClient;

// Connection URL
const url = `mongodb://testnet.bigchain.fptu.tech:27017`;

// Database Name
const dbName = "bigchain";

// Create a new MongoClient
const client = new MongoClient(url, { promiseLibrary: Promise });

export let db;

export async function initBigchainMongoDB() {
    // Use connect method to connect to the Server
    return new Promise(function(resolve, reject) {
        client.connect(function(err) {
            if (err) {
                reject(new Error("Cannot connect to BigchainDB MongoDB"));
            }

            console.log("MongoDB connected to BigchainDB MongoDB");
            db = client.db(dbName);

            resolve();

            // client.close();
        });
    });
}

// import { db } from "@models/bigchain";
// const array = await db.collection("transactions").find({
//     public_key: "123"
// }).toArray();
