import errorHandler from "@core/handlers/error.handler";
import transferService from "@services/transfer.service";
import outputService from "@services/output.service";
import bookService from "@services/book.service";
import importDBQueue from "@queues/importdb.queue";

import asset from "@core/bigchaindb/asset";

async function test(_, res) {
    try {
        const test = await asset.getPublicKeyFromEmail(
            "tuhmse62531@fpt.edu.vn"
        );

        res.send(test);
    } catch (err) {
        res.status(400).send({
            message:
                err instanceof Error
                    ? err.toString()
                    : err instanceof Error
                    ? err.toString()
                    : err
        });
    }
}

async function create(req, res) {
    const body = req.body;

    try {
        res.send(await transferService.createTestBook(body.public_key));
    } catch (err) {
        res.status(400);
        res.send({
            message: err instanceof Error ? err.toString() : err
        });
    }
}

async function transfer(req, res) {
    const body = req.body;

    try {
        res.send(
            await transferService.transferTestBook(
                body.asset_id,
                body.public_key
            )
        );
    } catch (err) {
        res.status(400);
        res.send({
            message: err instanceof Error ? err.toString() : err
        });
    }
}

async function sign(req, res) {
    const body = req.body;

    try {
        res.send(transferService.signTx(body.tx, body.private_key));
    } catch (err) {
        res.status(400);
        res.send({
            message: err instanceof Error ? err.toString() : err
        });
    }
}

async function post(req, res) {
    const body = req.body;

    try {
        res.send(await transferService.postTx(body.tx));
    } catch (err) {
        res.status(400);
        res.send({
            message: err instanceof Error ? err.toString() : err
        });
    }
}

async function getSpent(req, res) {
    const body = req.body;

    try {
        res.send(await outputService.getSpent(body.public_key));
    } catch (err) {
        res.status(400);
        res.send({
            message: err instanceof Error ? err.toString() : err
        });
    }
}

async function getUnspent(req, res) {
    const body = req.body;

    try {
        res.send(await outputService.getUnspent(body.public_key));
    } catch (err) {
        res.status(400);
        res.send({
            message: err instanceof Error ? err.toString() : err
        });
    }
}

async function searchBook(req, res) {
    const body = req.body;

    try {
        res.send(await bookService.searchBook(body.book_id));
    } catch (err) {
        res.status(400);
        res.send({
            message: err instanceof Error ? err.toString() : err
        });
    }
}

async function dbTest(_, res) {
    try {
        await importDBQueue.addJob();

        res.send({
            message: "Done!"
        });
    } catch (err) {
        res.status(400).send({
            message: err instanceof Error ? err.toString() : err.toString()
        });
    }
}

export default errorHandler({
    test,
    create,
    transfer,
    sign,
    post,
    getSpent,
    getUnspent,
    searchBook,
    dbTest
});
