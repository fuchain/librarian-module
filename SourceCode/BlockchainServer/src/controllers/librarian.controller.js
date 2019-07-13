import errorHandler from "@core/handlers/error.handler";
import bookService from "@services/book.service";
import userService from "@services/user.service";

async function getAllBookDetails(_, res) {
    const listBookDetails = await bookService.getAllBookDetail();

    res.send(listBookDetails);
}

async function getAllUsers(_, res) {
    const users = await userService.getAllUsers();

    res.send(users);
}

export default errorHandler({
    getAllBookDetails,
    getAllUsers
});
