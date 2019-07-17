import Queue from "bull";
import env from "@core/env";
const pairUpdateQueue = new Queue("pair", `redis://${env.redisHost}`);

// Dependency to run this queue
import { db } from "@models";

// Watch and Run job queue
function run() {
    pairUpdateQueue.process(jobCallback);
    pairUpdateQueue.on("completed", function(job, result) {
        console.log("Matched and updated!");
    });
}

// Describe what to do in the job
async function doJob(returner, requester) {
    try {
        if (!returner || !requester) return false;

        const matchingCollection = db.collection("matchings");

        await matchingCollection.updateOne(
            {
                email: returner.email,
                bookDetailId: returner.bookDetailId,
                bookId: returner.bookId,
                matched: false
            },
            {
                $set: {
                    matched: true,
                    matchWith: requester.email,
                    matchAt: Math.floor(Date.now() / 1000)
                }
            }
        );

        await matchingCollection.updateOne(
            {
                email: requester.email,
                bookDetailId: requester.bookDetailId,
                matched: false
            },
            {
                $set: {
                    matched: true,
                    matchWith: returner.email,
                    matchAt: Math.floor(Date.now() / 1000)
                }
            }
        );

        return true;
    } catch (err) {
        console.log("Error when pair update: ", err);
        throw err;
    }
}

async function jobCallback(job) {
    try {
        const { returner, requester } = job.data;
        const result = await doJob(returner, requester);

        return result;
    } catch (err) {
        throw err;
    }
}

async function addJob(returner, requester) {
    try {
        await pairUpdateQueue.add({ returner, requester });

        return true;
    } catch (err) {
        throw err;
    }
}

export default {
    run,
    addJob
};
