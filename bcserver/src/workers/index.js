import importDBWorker from "@workers/importdb.worker";
import matchingWorker from "@workers/matching.worker";
import pairWorker from "@workers/pair.worker";
import pairUpdateWorker from "@workers/pair.update.worker";
import insertWorker from "@workers/insert.worker";
import insertTxWorker from "@workers/insert.tx.worker";
import checkWorker from "@workers/check.worker";

export default async function initWorkers() {
    try {
        await matchingWorker.run();
        await importDBWorker.run();
        await pairWorker.run();
        await pairUpdateWorker.run();
        await insertWorker.run();
        await insertTxWorker.run();
        await checkWorker.run();

        // Pair queue will run every 1 minute
        await pairWorker.addJob();
    } catch (err) {
        throw err;
    }
}
