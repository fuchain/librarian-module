import Queue from "bull";
import env from "@core/env";
const insertTxQueue = new Queue("insertTx", `redis://${env.redisHost}`);

// Dependency to run this queue
import transferService from "@services/transfer.service";

// Watch and Run job queue
function run() {
    insertTxQueue.process(jobCallback);
}

// Describe what to do in the job
async function doJob(id) {
    try {
        const tx = await transferService.createBookForBookDetailId(id);
        console.log(`Added a book for book_detail ${id}, tx_id: ${tx.id}`);

        return tx;
    } catch (err) {
        console.log(`Something failed: ${err}`);
    }
}

async function jobCallback(job) {
    await doJob(job.data.id);
}

async function addJob(id) {
    try {
        await insertTxQueue.add({ id });
    } catch (err) {
        console.log(`Something failed when add job: ${err}`);
    }
}

export default {
    run,
    addJob
};
