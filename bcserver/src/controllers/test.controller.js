import errorHandler from "@core/handlers/error.handler";
import transferLogic from "@logics/transfer.logic";
import outputLogic from "@logics/output.logic";
import bookLogic from "@logics/book.logic";
import importDBQueue from "@workers/importdb.worker";

import insertQueue from "@workers/insert.worker";

async function test(_, res) {
    await insertQueue.addJob();

    res.send({
        message: "Done mate!"
    });
}

async function createTestAsset(_, res) {
    const testAsset = await transferLogic.createTestAsset();

    res.send(testAsset);
}

async function createTestBook(_, res) {
    const testBook = await transferLogic.createAndPostTestBook();

    res.send(testBook);
}

async function transferTestBook(_, res) {
    const testBookTransfer = await transferLogic.transferAndPostTestBook();

    res.send(testBookTransfer);
}

async function transferTestBookFraud(_, res) {
    const testBookTransfer = await transferLogic.transferAndPostTestBook(true);

    res.send(testBookTransfer);
}

async function create(req, res) {
    const body = req.body;

    res.send(await transferLogic.createTestBook(body.public_key));
}

async function transfer(req, res) {
    const body = req.body;

    res.send(
        await transferLogic.transferTestBook(body.asset_id, body.public_key)
    );
}

async function sign(req, res) {
    const body = req.body;

    res.send(transferLogic.signTx(body.tx, body.private_key));
}

async function post(req, res) {
    const body = req.body;

    res.send(await transferLogic.postTx(body));
}

async function getSpent(req, res) {
    const body = req.body;

    res.send(await outputLogic.getSpent(body.public_key));
}

async function getUnspent(req, res) {
    const body = req.body;

    res.send(await outputLogic.getUnspent(body.public_key));
}

async function searchBook(req, res) {
    const body = req.body;

    res.send(await bookLogic.searchBook(body.book_id));
}

async function dbTest(_, res) {
    await importDBQueue.addJob();

    res.send({
        message: "Done!"
    });
}

export default errorHandler({
    test,
    createTestAsset,
    createTestBook,
    transferTestBook,
    transferTestBookFraud,
    create,
    transfer,
    sign,
    post,
    getSpent,
    getUnspent,
    searchBook,
    dbTest
});
