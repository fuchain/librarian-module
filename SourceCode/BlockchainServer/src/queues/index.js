import importDBQueue from "@queues/importdb.queue";
import matchingQueue from "@queues/matching.queue";
import pairQueue from "@queues/pair.queue";
import pairUpdateQueue from "@queues/pair.update.queue";
import insertQueue from "@queues/insert.queue";
import insertTxQueue from "@queues/insert.tx.queue";

export default async function initQueues() {
    try {
        await matchingQueue.run();
        await importDBQueue.run();
        await pairQueue.run();
        // Pair queue will run every 1 minute
        await pairQueue.addJob({ cron: "* * * * *" });
        await pairUpdateQueue.run();
        await insertQueue.run();
        await insertTxQueue.run();
    } catch (err) {
        throw err;
    }
}