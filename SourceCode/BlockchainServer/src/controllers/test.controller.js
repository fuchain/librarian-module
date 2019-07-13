import errorHandler from "@core/handlers/error.handler";
import transferService from "@services/transfer.service";
import outputService from "@services/output.service";
import bookService from "@services/book.service";
import importDBQueue from "@queues/importdb.queue";

import insertQueue from "@queues/insert.queue";

async function test(_, res) {
    await insertQueue.addJob();

    res.send({
        message: "Done mate!"
    });
}

async function createTestAsset(_, res) {
    const testAsset = await transferService.createTestAsset();

    res.send(testAsset);
}

async function createTestBook(_, res) {
    const testBook = await transferService.createAndPostTestBook();

    res.send(testBook);
}

async function transferTestBook(_, res) {
    const testBookTransfer = await transferService.transferAndPostTestBook();

    res.send(testBookTransfer);
}

async function create(req, res) {
    const body = req.body;

    res.send(await transferService.createTestBook(body.public_key));
}

async function transfer(req, res) {
    const body = req.body;

    res.send(
        await transferService.transferTestBook(body.asset_id, body.public_key)
    );
}

async function sign(req, res) {
    const body = req.body;

    res.send(transferService.signTx(body.tx, body.private_key));
}

async function post(req, res) {
    const body = req.body;

    res.send(await transferService.postTx(body));
}

async function getSpent(req, res) {
    const body = req.body;

    res.send(await outputService.getSpent(body.public_key));
}

async function getUnspent(req, res) {
    const body = req.body;

    res.send(await outputService.getUnspent(body.public_key));
}

async function searchBook(req, res) {
    const body = req.body;

    res.send(await bookService.searchBook(body.book_id));
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
    create,
    transfer,
    sign,
    post,
    getSpent,
    getUnspent,
    searchBook,
    dbTest
});
