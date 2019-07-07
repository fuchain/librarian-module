import Queue from "bull";
const matchingQueue = new Queue("matching");

// Dependency to run this queue
import { db } from "@models";

// Watch and Run job queue
function run() {
    matchingQueue.process(jobCallback);
    matchingQueue.on("completed", function(job, result) {
        console.log(`Job ${job.id} done for requester: ${result.email}`);
    });
}

// Describe what to do in the job
async function doJob(email, bookDetailId, bookId) {
    try {
        const matchingCollection = db.collection("matchings");
        const requestObj = {
            email,
            bookDetailId,
            bookId
        };

        // Check is request is existed
        const requested = await matchingCollection.findOne(requestObj);
        if (requested) {
            throw new Error("Duplicated!");
        }

        // Add timestamp and matched flag
        requestObj.matched = false;
        requestObj.time = Math.floor(Date.now() / 1000);
        await matchingCollection.insertMany([requestObj]);

        return { email, bookDetailId, bookId };
    } catch (err) {
        throw err;
    }
}

async function jobCallback(job) {
    try {
        const { email, bookDetailId, bookId } = job.data;
        const result = await doJob(email, bookDetailId, bookId);

        return result;
    } catch (err) {
        throw err;
    }
}

async function addJob(email, bookDetailId, bookId) {
    try {
        await matchingQueue.add({
            email,
            bookDetailId,
            bookId
        });

        return true;
    } catch (err) {
        throw err;
    }
}

export default {
    run,
    addJob
};
