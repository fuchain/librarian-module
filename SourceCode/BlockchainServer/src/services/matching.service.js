import matchingQueue from "@queues/matching.queue";

async function createMatchingRequest(email, bookDetailId, bookId) {
    // Constraint
    if (!email || !bookDetailId) {
        throw new Error("Invalid request");
    }

    await matchingQueue.addJob(email, bookDetailId, bookId);

    return true;
}

export default { createMatchingRequest };
