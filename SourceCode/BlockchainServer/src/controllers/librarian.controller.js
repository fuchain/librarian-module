import errorHandler from "@core/handlers/error.handler";
import bookService from "@services/book.service";
import userService from "@services/user.service";
import outputService from "@services/output.service";

async function getAllBookDetails(_, res) {
    const listBookDetails = await bookService.getAllBookDetail();

    res.send(listBookDetails);
}

async function getAllUsers(_, res) {
    const users = await userService.getAllUsers('reader');

    res.send(users);
}

async function getBookByUser(req, res) {
    const public_key = req.body.public_key;

    const bookdetailList = await userService.getCurrentBook(public_key);

    res.send(bookdetailList);
}
async function getBookInstanceList(req, res){
    const book_detail_id = req.params.book_detail_id;
    const bookList = await bookService.getBookInstanceList(book_detail_id);
    res.send(bookList);
}

export default errorHandler({
    getAllBookDetails,
    getAllUsers,
    getBookByUser,
    getBookInstanceList
});
