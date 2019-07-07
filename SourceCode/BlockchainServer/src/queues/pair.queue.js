import Queue from "bull";
const pairQueue = new Queue("pair");

// Dependency to run this queue
import { db } from "@models";

// Watch and Run job queue
function run() {
    pairQueue.process(jobCallback);
}

function makeAUniqueArray(arr) {
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
            throw new Error("Nothing to do!");
        }

        // List Book Details
        const bookDetailsArrUnique = makeAUniqueArray(notMatchedArr);

        const pairArr = bookDetailsArrUnique.map(e => {
            const temp = notMatchedArr.filter(el => {
                return el.bookDetailId === e;
            });

            return temp;
        });

        pairArr.map(bArr => {
            const returnArr = bArr.filter(match => !match.bookId);
            const requestArr = bArr.filter(match => match.bookId);

            returnArr.sort((a, b) => b.time - a.time);
            requestArr.sort((a, b) => b.time - a.time);

            const shorterLength =
                returnArr.length < requestArr.length
                    ? returnArr.length
                    : requestArr.length;

            return Array.from(Array(shorterLength)).map(async (_, index) => {
                // This is a match!
                const matchedReturner = returnArr[index];
                const matchedRequester = requestArr[index];

                await matchingCollection.updateOne(matchedReturner, {
                    $set: {
                        matched: true,
                        matchWith: matchedRequester.email
                    }
                });

                await matchingCollection.updateOne(matchedRequester, {
                    $set: {
                        matched: true,
                        matchWith: matchedReturner.email
                    }
                });
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

async function addJob() {
    try {
        await pairQueue.add(null, { repeat: { cron: "* * * * *" } });

        return true;
    } catch (err) {
        throw err;
    }
}

export default {
    run,
    addJob
};
