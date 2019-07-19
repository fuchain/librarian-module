import errorHandler from "@core/handlers/error.handler";
import bookService from "@services/book.service";
import userService from "@services/user.service";
import transferService from "@services/transfer.service";
import { db } from "@models";

async function getAllBookDetails(_, res) {
    const listBookDetails = await bookService.getAllBookDetail();

    res.send(listBookDetails);
}

async function getAllUsers(_, res) {
    const users = await userService.getAllUsers("reader");

    res.send(users);
}

async function getBookByUser(req, res) {
    const public_key = req.body.public_key;

    const bookdetailList = await userService.getCurrentBook(public_key);

    res.send(bookdetailList);
}

async function getBookInstanceList(req, res) {
    const book_detail_id = req.params.book_detail_id;
    const bookList = await bookService.getBookInstanceList(
        book_detail_id,
        true
    );
    res.send(bookList);
}

async function getHistoryOfBookInstance(req, res) {
    const bookId = req.body.book_id;
    const transactionList = await bookService.getHistoryOfBookInstance(bookId);
    if (!transactionList) {
        return res.status(400).send({
            message: "Cannot find any asset with book id = " + bookId
        });
    }

    res.send(transactionList);
}

async function getOverview(_, res) {
    const bookDetailsCollection = db.collection("book_details");
    const bookDetailTotal = await bookDetailsCollection.find().count();

    const bookInstanceTotal = await bookService.getBookInstanceTotal("book");

    const userTotal = await userService.getUserTotal("reader");

    res.send({
        book_detail_total: bookDetailTotal,
        book_instance_total: bookInstanceTotal,
        user_total: userTotal
    });
}

// Get book instance total of book detail at librarian
async function getBookTotalByBDID(req, res) {
    let total = 0;
    const bookDetailId = req.body.book_detail_id;

    const bookList = await bookService.getBookInstanceList(bookDetailId);
    if (bookList.length) {
        total = bookList.length;
    }

    res.send({
        total
    });
}

// Get book instance total by book detail id at library
async function getBookTotalAtLib(req, res) {
    const bookDetailId = req.body.book_detail_id;

    const total = await bookService.getBookTotalAtLib(bookDetailId);

    res.send({
        total: total
    });
}

async function giveBook(req, res) {
    const bookDetailId = req.body.book_detail_id;
    const bookList = await bookService.getBookInstanceList(bookDetailId);
    const book = bookList[0];

    const tx = await transferService.createTransferRequest(
        book.id,
        req.body.to.email
    );

    res.send(tx);
}

export default errorHandler({
    getAllBookDetails,
    getAllUsers,
    getBookByUser,
    getBookInstanceList,
    getHistoryOfBookInstance,
    getOverview,
    getBookTotalByBDID,
    getBookTotalAtLib,
    giveBook
});
