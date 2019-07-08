import errorHandler from "@core/handlers/error.handler";
import bookService from "@services/book.service";

async function getAllBookDetails(_, res) {
    try {
        const listBookDetails = await bookService.getAllBookDetail();

        res.send(listBookDetails);
    } catch (err) {
        res.status(400);
        res.send({
            message: err instanceof Error ? err.toString() : err
        });
    }
}

export default errorHandler({
    getAllBookDetails
});
