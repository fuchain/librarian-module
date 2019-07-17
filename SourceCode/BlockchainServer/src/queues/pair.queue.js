import Queue from "bull";
import env from "@core/env";
const pairQueue = new Queue("pair", `redis://${env.redisHost}`);

// Dependency to run this queue
import { db } from "@models";
import { addJob as addJobPairUpdate } from "@queues/pair.update.queue";

// Watch and Run job queue
function run() {
    pairQueue.process(jobCallback);
}

function makeDistictArray(arr) {
    const arrFiltered = arr.map(e => e.bookDetailId);
    return Array.from(new Set(arrFiltered));
}

// Describe what to do in the job
async function doJob() {
    try {
        const matchingCollection = db.collection("matchings");

        const notMatchedArr = await matchingCollection
            .find({
                matched: false
            })
            .toArray();

        if (!notMatchedArr.length) {
            return false;
        }

        // Get book detail distinct from the not matched elements
        const bookDetailsIdsUnique = makeDistictArray(notMatchedArr);

        // Create a queue for each book detail, each queue have two array (for returner and requester)
        const queuesByBookDetails = bookDetailsIdsUnique.map(e => {
            const bookDetailQueue = notMatchedArr.filter(el => {
                return el.bookDetailId === e;
            });

            return bookDetailQueue;
        });

        // Query in each book detail queue to get a match couple
        queuesByBookDetails.map(aBookDetailQueue => {
            const returnArr = aBookDetailQueue.filter(match => !match.bookId);
            const requestArr = aBookDetailQueue.filter(match => match.bookId);

            returnArr.sort((a, b) => b.time - a.time);
            requestArr.sort((a, b) => b.time - a.time);

            const shorterLength =
                returnArr.length < requestArr.length
                    ? returnArr.length
                    : requestArr.length;

            if (!shorterLength) {
                return false;
            }

            const loopByShorterLength = Array.from(Array(shorterLength));
            loopByShorterLength.map((_, index) => {
                // This is a match!
                const matchedReturner = returnArr[index];
                const matchedRequester = requestArr[index];

                // Add a job to update db and push
                addJobPairUpdate(matchedReturner, matchedRequester);
            });
        });

        return true;
    } catch (err) {
        console.log("Error when pair: ", err);
        throw err;
    }
}

async function jobCallback(_) {
    try {
        const result = await doJob();

        return result;
    } catch (err) {
        throw err;
    }
}

async function addJob(cron) {
    try {
        await pairQueue.add(null, { repeat: cron });

        return true;
    } catch (err) {
        throw err;
    }
}

export default {
    run,
    addJob
};
