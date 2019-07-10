import errorHandler from "@core/handlers/error.handler";
import bookService from "@services/book.service";

async function searchBookDetails(req, res) {
    const search = req.query.text;

    const listBookDetails = await bookService.searchBookDetail(search);

    res.send(listBookDetails);
}

export default errorHandler({
    searchBookDetails
});
