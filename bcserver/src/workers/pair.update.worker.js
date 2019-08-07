import Queue from "bull";
import env from "@core/env";
const pairUpdateQueue = new Queue("pairUpdate", `redis://${env.redisHost}`);

// Dependency to run this queue
import userLogic from "@logics/user.logic";
import bookLogic from "@logics/book.logic";
import { db } from "@models";
import { request } from "http";
import axios from "axios";

// Watch and Run job queue
function run() {
    pairUpdateQueue.process(jobCallback);
}

// Describe what to do in the job
async function doJob(returner, requester) {
    try {
        if (!returner || !requester) return false;

        const matchingCollection = db.collection("matchings");

        returner.phone = await userLogic.getPhoneFromEmail(returner.email);
        requester.phone = await userLogic.getPhoneFromEmail(request.email);

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
                    matchPhone: requester.phone,
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
                    matchPhone: returner.phone,
                    matchAt: Math.floor(Date.now() / 1000)
                }
            }
        );

        // Push notification
        const bookDetail = await bookLogic.getBookDetail(returner.bookDetailId);

        axios.post(`${env.ioHost}/notifications/push`, {
            email: requester.email,
            type: "returning",
            message: `Yêu cầu trả sách ${bookDetail.name ||
                returner.bookDetailId} của bạn đã được ghép với ${
                returner.email
            }`
        });

        axios.post(`${env.ioHost}/notifications/push`, {
            email: returner.email,
            type: "requesting",
            message: `Yêu cầu mượn sách ${bookDetail.name ||
                returner.bookDetailId} của bạn đã được ghép với ${
                requester.email
            }`
        });

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
