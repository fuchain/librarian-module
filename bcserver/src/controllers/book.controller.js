import errorHandler from "@core/handlers/error.handler";
import bookService from "@services/book.service";

async function searchBookDetails(req, res) {
    const search = req.query.text;

    const listBookDetails = await bookService.searchBookDetail(search);

    res.send(listBookDetails);
}

async function getBookDetail(req, res) {
    const id = req.params.id;

    const bookDetail = await bookService.getBookDetail(id);

    if (!bookDetail) {
        res.status(422).send({
            message: "Cannot find that book detail"
        });
    }

    res.send(bookDetail);
}

export default errorHandler({
    searchBookDetails,
    getBookDetail
});
