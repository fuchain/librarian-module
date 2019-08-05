import Queue from "bull";
import env from "@core/env";
const pairQueue = new Queue("pair", `redis://${env.redisHost}`);

// Dependency to run this queue
import axios from "axios";

// Watch and Run job queue
function run() {
    pairQueue.process(jobCallback);
}

// Describe what to do in the job
async function doJob() {
    try {
        // Node.js queue logic at: /Document/LegacyWorker/pair.js

        // Call Golang Pair Worker
        const { data } = await axios.get("http://ssh.fptu.tech:5100");
        console.log("Pairworker: ", data);

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
        pairQueue.add(null, { repeat: { cron: "* * * * *" } });

        return true;
    } catch (err) {
        throw err;
    }
}

export default {
    run,
    addJob
};
