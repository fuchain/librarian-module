import Queue from "bull";
import env from "@core/env";
const matchingQueue = new Queue("matching", `redis://${env.redisHost}`);

// Dependency to run this queue
import { db } from "@core/db";
import axios from "axios";
import asset from "@core/fuchain/asset";
import userLogic from "@logics/user.logic";
import rejectLogic from "@logics/reject.logic";
import constants from "@core/constants";
import checkWorker from "@workers/check.worker";

// Watch and Run job queue
function run() {
    matchingQueue.process(jobCallback);
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

        // Check reject count is limited
        if (bookId) {
            const rejectCount = await rejectLogic.getRejectCount(bookId);

            if (rejectCount > constants.REJECT_LIMIT) {
                axios.post(`${env.ioHost}/events/push`, {
                    email,
                    type: "fail",
                    message:
                        "Sách của bạn đã bị từ chối quá nhiều lẫn, vui lòng mang sách đến thư viện để kiểm tra"
                });

                throw new Error("Reject limit, cannot create request!");
            }
        }

        // Check is request is existed
        const requested = await matchingCollection.findOne(requestObj);

        const publicKey = await asset.getPublicKeyFromEmail(email);
        const currentKeepingBooks = await userLogic.getCurrentBook(
            publicKey,
            true
        );

        if (!isCancel) {
            const isActive = await userLogic.isUserActive(email);
            if (!isActive && !bookId) {
                axios.post(`${env.ioHost}/events/push`, {
                    email,
                    type: "fail",
                    message:
                        "Tài khoản của bạn đang bị tạm khóa, vui lòng liên hệ thư viện"
                });
                throw new Error("Not valid request for disabled account!");
            }

            if (!bookId && currentKeepingBooks.length > 5) {
                axios.post(`${env.ioHost}/events/push`, {
                    email,
                    type: "fail",
                    message:
                        "Bạn đang giữ quá 5 sách rồi, không thể yêu cầu mượn thêm"
                });
                throw new Error("Not valid request!");
            }

            if (
                !bookId &&
                currentKeepingBooks.find(e => e.book_detail.id === bookDetailId)
            ) {
                axios.post(`${env.ioHost}/events/push`, {
                    email,
                    type: "fail",
                    message: "Bạn đang giữ sách đó rồi, không thể yêu cầu thêm"
                });
                throw new Error("Not valid request!");
            }

            if (requested) {
                axios.post(`${env.ioHost}/events/push`, {
                    email,
                    type: "fail",
                    message:
                        "Bạn đã có yêu cầu mượn sách đó rồi, không thể gửi thêm yêu cầu"
                });
                throw new Error("Duplicated!");
            }

            // Add timestamp and matched flag
            requestObj.matched = false;
            requestObj.time = Math.floor(Date.now() / 1000);
            await matchingCollection.insertMany([requestObj]);

            axios.post(`${env.ioHost}/events/push`, {
                email,
                type: "success",
                message: `Gửi yêu cầu ${
                    requestObj.bookId ? "trả" : "mượn"
                } sách thành công`
            });

            // Check book is available
            if (!bookId && !isCancel) {
                console.log("Add job", email, bookDetailId);
                checkWorker.addJob(email, bookDetailId);
            }

            return { email, bookDetailId, bookId };
        } else {
            if (!requested) {
                throw new Error("Cannot find to delete!");
            }

            await matchingCollection.deleteOne(requestObj);

            axios.post(`${env.ioHost}/events/push`, {
                email,
                type: "success",
                message: "Hủy yêu cầu mượn sách thành công"
            });

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
