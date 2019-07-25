// Dependencies to run this queue
import { db } from "@models";
import pairUpdateQueue from "@queues/pair.update.queue";

const matchingCollection = db.collection("matchings");

function makeDistictArray(arr) {
  const arrFiltered = arr.map(e => e.bookDetailId);
  return Array.from(new Set(arrFiltered));
}

const notMatchedArr = await matchingCollection
  .find({
    matched: false
  })
  .toArray();

if (!notMatchedArr.length) {
  return false;
}

// Get book detail distinct from the not matched elements
const bookDetailsIdsUnique = makeDistictArray(notMatchedArr);

// Create a queue for each book detail, each queue have two array (for returner and requester)
const queuesByBookDetails = bookDetailsIdsUnique.map(e => {
  const bookDetailQueue = notMatchedArr.filter(el => {
    return el.bookDetailId === e;
  });

  return bookDetailQueue;
});

// Query in each book detail queue to get a match couple
queuesByBookDetails.forEach(aBookDetailQueue => {
  const returnArr = aBookDetailQueue.filter(match => !match.bookId);
  const requestArr = aBookDetailQueue.filter(match => match.bookId);

  returnArr.sort((a, b) => b.time - a.time);
  requestArr.sort((a, b) => b.time - a.time);

  const shorterLength =
    returnArr.length < requestArr.length ? returnArr.length : requestArr.length;

  if (!shorterLength) {
    return false;
  }

  const loopByShorterLength = Array.from(Array(shorterLength));
  loopByShorterLength.forEach((_, index) => {
    // This is a match!
    const matchedReturner = returnArr[index];
    const matchedRequester = requestArr[index];

    // Add a job to update db and push
    pairUpdateQueue.addJob(matchedReturner, matchedRequester);
  });
});
