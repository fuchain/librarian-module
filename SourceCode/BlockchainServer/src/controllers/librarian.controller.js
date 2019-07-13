import errorHandler from "@core/handlers/error.handler";
import bookService from "@services/book.service";

async function getAllBookDetails(_, res) {
    const listBookDetails = await bookService.getAllBookDetail();

    res.send(listBookDetails);
}

function getAllUsers(req, res){
    res.send('getAllUsers');
}

export default errorHandler({
    getAllBookDetails,
    getAllUsers
});
