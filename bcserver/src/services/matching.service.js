import matchingQueue from "@workers/matching.worker";
import bookService from "@services/book.service";
import asset from "@core/fuchain/asset";
import { db } from "@models";

async function createMatchingRequest(publicKey, bookDetailId, bookId) {
    const email = await asset.getEmailFromPublicKey(publicKey);

    // Constraint
    if (!email || !bookDetailId) {
        throw new Error("Invalid request");
    }

    await matchingQueue.addJob(email, bookDetailId, bookId);

    return true;
}

async function cancelMatchingRequest(publicKey, bookDetailId, bookId) {
    const email = await asset.getEmailFromPublicKey(publicKey);

    // Constraint
    if (!email || !bookDetailId) {
        throw new Error("Invalid request");
    }

    await matchingQueue.addJob(email, bookDetailId, bookId, true);

    return true;
}

async function getMatchings() {
    const matchingCollection = db.collection("matchings");

    const matchings = await matchingCollection.find().toArray();

    const result = matchings.map(async e => {
        const bookInfo = await bookService.getBookDetail(e.bookDetailId);
        e.bookInfo = bookInfo;
        return e;
    });

    return await Promise.all(result);
}

export default { createMatchingRequest, cancelMatchingRequest, getMatchings };
