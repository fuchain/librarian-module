import Queue from "bull";
const matchingQueue = new Queue("matching");

// Dependency to run this queue
import { db } from "@models";

// Watch and Run job queue
function run() {
    matchingQueue.process(jobCallback);
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
            console.log("Duplicate request!");

            throw new Error("Duplicated!");
        }

        await matchingCollection.insertMany([requestObj]);

        console.log("Done add new request to DB!");

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
