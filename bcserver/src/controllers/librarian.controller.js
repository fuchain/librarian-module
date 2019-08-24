import errorHandler from "@core/handlers/error.handler";
import bookLogic from "@logics/book.logic";
import userLogic from "@logics/user.logic";
import transferLogic from "@logics/transfer.logic";
import { db } from "@core/db";

async function getAllBookDetails(req, res) {
    const text = req.query.text;

    if (text) {
        const listBookDetails = await bookLogic.searchBookDetail(text);
        res.send(listBookDetails);
    } else {
        const listBookDetails = await bookLogic.getAllBookDetail();
        res.send(listBookDetails);
    }
}

async function getAllUsers(_, res) {
    const users = await userLogic.getAllUsers("reader");

    res.send(users);
}

async function getBookByUser(req, res) {
    const public_key = req.body.user.public_key;

    const bookdetailList = await userLogic.getCurrentBook(public_key, true);

    res.send(bookdetailList);
}

async function getBookInstanceList(req, res) {
    const book_detail_id = req.params.book_detail_id;
    const bookList = await bookLogic.getBookInstanceList(book_detail_id);

    res.send(bookList);
}

async function getBookInstanceDetailList(req, res) {
    const book_detail_id = req.params.book_detail_id;
    const bookList = await bookLogic.getBookInstanceDetailList(book_detail_id);

    res.send(bookList);
}

async function getHistoryOfBookInstance(req, res) {
    const bookId = req.body.book_id;
    const transactionList = await bookLogic.getHistoryOfBookInstance(bookId);

    transactionList
        ? res.send(transactionList)
        : res.status(400).send({
              message: "Cannot find any asset with book id = " + bookId
          });
}

async function getOverview(_, res) {
    const bookDetailsCollection = db.collection("book_details");
    const bookDetailTotal = await bookDetailsCollection.find().count();

    const bookInstanceTotal = await bookLogic.getBookInstanceTotal("book");

    const userTotal = await userLogic.getUserTotal("reader");

    res.send({
        book_detail_total: bookDetailTotal,
        book_instance_total: bookInstanceTotal,
        user_total: userTotal
    });
}

// Get book instance total of book detail at librarian
async function getBookTotalByBDID(req, res) {
    const bookDetailId = req.body.book_detail_id;

    const bookList = await bookLogic.getBookInstanceList(bookDetailId);

    const total = bookList.length || 0;

    res.send({
        total
    });
}

// Get book instance total by book detail id at library
async function getBookTotalAtLib(req, res) {
    const bookDetailId = req.body.book_detail_id;

    const total = await bookLogic.getBookTotalAtLib(bookDetailId);

    res.send({
        total
    });
}

async function giveBook(req, res) {
    const bookDetailId = req.body.book_detail_id;
    const bookList = await bookLogic.getBookAtLib(bookDetailId);
    const book = bookList[0];

    const tx = await transferLogic.createTransferRequest(
        book.asset_id,
        req.body.to.email
    );

    res.send(tx);
}

async function recoverAccount(req, res) {
    const email = req.body.user.email;
    const newPublicKey = req.body.user.new_public_key;

    await transferLogic.recoverAccount(email, newPublicKey);

    res.send({
        message: "Done"
    });
}

async function lockAccount(req, res) {
    const email = req.body.email;

    const status = await userLogic.lockAccount(email);

    res.send({
        message: "Done",
        status: status ? "inactive" : "active"
    });
}

async function searchUser(req, res) {
    const text = req.body.text;

    const result = await userLogic.searchUser(text);

    res.send(result);
}

export default errorHandler({
    getAllBookDetails,
    getAllUsers,
    getBookByUser,
    getBookInstanceList,
    getBookInstanceDetailList,
    getHistoryOfBookInstance,
    getOverview,
    getBookTotalByBDID,
    getBookTotalAtLib,
    giveBook,
    recoverAccount,
    lockAccount,
    searchUser
});
