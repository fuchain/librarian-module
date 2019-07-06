import transactionService from "@services/transaction.service";
import outputService from "@services/output.service";
import bookService from "@services/book.service";

const create = async (req, res) => {
    const body = req.body;

    try {
        res.send(await transactionService.createTestBook(body.public_key));
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
};

const transfer = async (req, res) => {
    const body = req.body;

    try {
        res.send(
            await transactionService.transferTestBook(
                body.asset_id,
                body.public_key
            )
        );
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
};

const sign = (req, res) => {
    const body = req.body;

    try {
        res.send(transactionService.signTx(body.tx, body.private_key));
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
};

const post = async (req, res) => {
    const body = req.body;

    try {
        res.send(await transactionService.postTx(body.tx));
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
};

const getSpent = async (req, res) => {
    const body = req.body;

    try {
        res.send(await outputService.getSpent(body.public_key));
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
};

const getUnspent = async (req, res) => {
    const body = req.body;

    try {
        res.send(await outputService.getUnspent(body.public_key));
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
};

const searchBook = async (req, res) => {
    const body = req.body;

    try {
        res.send(await bookService.searchBook(body.book_id));
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
};

export default {
    create,
    transfer,
    sign,
    post,
    getSpent,
    getUnspent,
    searchBook
};
