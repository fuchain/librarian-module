import Queue from "bull";
import env from "@core/env";
const insertQueue = new Queue("insertBook", `redis://${env.redisHost}`);

// Dependency to run this queue
import { db } from "@core/db";
import insertTxQueue from "@workers/insert.tx.worker";
import asset from "@core/fuchain/asset";
import concurrencyHandler from "@core/handlers/concurrency.handler";

// Watch and Run job queue
function run() {
    insertQueue.process(jobCallback);
}

// Describe what to do in the job
export async function doJob() {
    try {
        // Run query
        const collection = db.collection("book_details");

        const bookDetails = await collection.find().toArray();

        const result = await concurrencyHandler(bookDetails, 5, async e => {
            const bookDetailIdSearch = await asset.searchAsset(e.id);
            const bookList = bookDetailIdSearch.filter(e => e.data.book_detail);

            const numOfJobs = e.amount - bookList.length;

            return {
                bookDetailId: e.id,
                numOfJobs
            };
        });

        result.forEach(e => {
            if (e.numOfJobs)
                for (let i = 0; i < e.numOfJobs; i++) {
                    insertTxQueue.addJob(e.bookDetailId);
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
