import matchingQueue from "@queues/matching.queue";
import asset from "@core/bigchaindb/asset";

async function createMatchingRequest(publicKey, bookDetailId, bookId) {
    const email = await asset.getEmailFromPublicKey(publicKey);

    // Constraint
    if (!email || !bookDetailId) {
        throw new Error("Invalid request");
    }

    await matchingQueue.addJob(email, bookDetailId, bookId);

    return true;
}

export default { createMatchingRequest };
