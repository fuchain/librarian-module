import Queue from "bull";
import env from "@core/env";
const checkQueue = new Queue("check", `redis://${env.redisHost}`);

// Dependency to run this queue
import { db } from "@core/db";
import axios from "axios";
import bookLogic from "@logics/book.logic";
import constants from "@core/constants";

// Watch and Run job queue
function run() {
    checkQueue.process(jobCallback);
}

// Describe what to do in the job
async function doJob(email, bookDetailId) {
    try {
        const bookDetail = await bookLogic.getBookDetail(bookDetailId);

        const matchingCollection = db.collection("matchings");
        const inQueueBooks = await matchingCollection
            .find({
                bookDetailId,
                bookId: {
                    $ne: null
                },
                matched: false
            })
            .toArray();
        const numAtQueue = inQueueBooks.length;
        const numAtLib = await bookLogic.getBookTotalAtLib(bookDetailId);

        if (!numAtLib && !numAtQueue) {
            axios.post(`${env.ioHost}/events/push`, {
                email,
                type: "notify",
                message: `Hiện tại không còn quyển ${bookDetail.name} nào, vui lòng tới thư viện và liên hệ thủ thư`
            });

            axios.post(`${env.ioHost}/notifications/push`, {
                email: constants.LIBRARIAN_EMAIL,
                type: "notify",
                message: `Người đọc ${emaill} đã yêu cầu mượn quyển ${bookDetail.name} nhưng không còn quyển nào khả dụng`
            });
        } else if (numAtQueue && !numAtLib) {
            axios.post(`${env.ioHost}/events/push`, {
                email,
                type: "notify",
                message: `Hiện đang có ${numAtQueue} người đọc đang trả sách ${bookDetail.name}`
            });
        } else if (!numAtQueue && numAtLib) {
            axios.post(`${env.ioHost}/events/push`, {
                email,
                type: "notify",
                message: `Hiện không có ai đang trả sách ${bookDetail.name}, vui lòng đến thư viện để nhận sách`
            });
        }

        return true;
    } catch (err) {
        console.log(err);
        throw err;
    }
}

async function jobCallback(job) {
    try {
        const { email, bookDetailId } = job.data;
        const result = await doJob(email, bookDetailId);

        return result;
    } catch (err) {
        throw err;
    }
}

async function addJob(email, bookDetailId) {
    try {
        await checkQueue.add({
            email,
            bookDetailId
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
