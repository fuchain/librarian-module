import Queue from "bull";
import env from "@core/env";
const insertQueue = new Queue("insertBook", `redis://${env.redisHost}`);

// Dependency to run this queue
import { db } from "@models";
import insertTxQueue from "@workers/insert.tx.worker";

// Watch and Run job queue
function run() {
    insertQueue.process(jobCallback);
}

// Describe what to do in the job
async function doJob() {
    try {
        // Run query
        const collection = db.collection("book_details");

        const bookDetails = await collection.find().toArray();

        // Add book instance
        bookDetails.forEach(e => {
            const amount = e.amount;
            for (let i = 0; i < amount; i++) {
                insertTxQueue.addJob(e.id);
            }
        });
    } catch (err) {
        console.log(`Something failed: ${err}`);
    }
}

async function jobCallback(_) {
    await doJob();
}

async function addJob() {
    try {
        await insertQueue.add();
    } catch (err) {
        console.log(`Something failed when add job: ${err}`);
    }
}

export default {
    run,
    addJob
};
