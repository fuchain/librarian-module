import matchingQueue from "@workers/matching.worker";
import bookLogic from "@logics/book.logic";
import asset from "@core/fuchain/asset";
import { db } from "@core/db";

async function createMatchingRequest(email, bookDetailId, bookId) {
    // Constraint
    if (!bookDetailId) {
        throw new Error("Invalid request");
    }

    await matchingQueue.addJob(email, bookDetailId, bookId);

    return true;
}

async function cancelMatchingRequest(email, bookDetailId, bookId) {
    // Constraint
    if (!bookDetailId) {
        throw new Error("Invalid request");
    }

    await matchingQueue.addJob(email, bookDetailId, bookId, true);

    return true;
}

async function getMatchings() {
    const matchingCollection = db.collection("matchings");

    const matchings = await matchingCollection.find().toArray();

    const result = matchings.map(async e => {
        const bookInfo = await bookLogic.getBookDetail(e.bookDetailId);
        e.bookInfo = bookInfo;
        return e;
    });

    return await Promise.all(result);
}

export default { createMatchingRequest, cancelMatchingRequest, getMatchings };
