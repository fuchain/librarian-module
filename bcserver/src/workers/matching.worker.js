import Queue from "bull";
import env from "@core/env";
const matchingQueue = new Queue("matching", `redis://${env.redisHost}`);

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
async function doJob(email, bookDetailId, bookId, isCancel) {
    try {
        const matchingCollection = db.collection("matchings");
        const requestObj = {
            email,
            bookDetailId,
            bookId: bookId || null
        };

        // Check is request is existed
        const requested = await matchingCollection.findOne(requestObj);

        if (!isCancel) {
            if (requested) {
                throw new Error("Duplicated!");
            }

            // Add timestamp and matched flag
            requestObj.matched = false;
            requestObj.time = Math.floor(Date.now() / 1000);
            await matchingCollection.insertMany([requestObj]);

            return { email, bookDetailId, bookId };
        } else {
            if (!requested) {
                throw new Error("Cannot find to delete!");
            }

            await matchingCollection.deleteOne(requestObj);

            return true;
        }
    } catch (err) {
        throw err;
    }
}

async function jobCallback(job) {
    try {
        const { email, bookDetailId, bookId, isCancel } = job.data;
        const result = await doJob(email, bookDetailId, bookId, isCancel);

        return result;
    } catch (err) {
        throw err;
    }
}

async function addJob(email, bookDetailId, bookId, isCancel = false) {
    try {
        await matchingQueue.add({
            email,
            bookDetailId,
            bookId,
            isCancel
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
