import Queue from "bull";
import env from "@core/env";
const pairUpdateQueue = new Queue("pair", `redis://${env.redisHost}`);

// Dependency to run this queue
import { db } from "@models";

// Watch and Run job queue
function run() {
    pairUpdateQueue.process(jobCallback);
}

// Describe what to do in the job
async function doJob(returner, requester) {
    try {
        if (!returner || !requester) return false;

        const matchingCollection = db.collection("matchings");

        await matchingCollection.updateOne(returner, {
            $set: {
                matched: true,
                matchWith: requester.email
            }
        });

        await matchingCollection.updateOne(requester, {
            $set: {
                matched: true,
                matchWith: returner.email
            }
        });

        return true;

        // Send event push for them here
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
