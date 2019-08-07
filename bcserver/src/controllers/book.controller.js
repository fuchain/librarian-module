import errorHandler from "@core/handlers/error.handler";
import bookLogic from "@logics/book.logic";

async function searchBookDetails(req, res) {
    const search = req.query.text;

    const listBookDetails = await bookLogic.searchBookDetail(search);

    res.send(listBookDetails);
}

async function getBookDetail(req, res) {
    const id = req.params.id;

    const bookDetail = await bookLogic.getBookDetail(id);

    bookDetail
        ? res.send(bookDetail)
        : res.status(422).send({
              message: "Cannot find that book detail"
          });
}

export default errorHandler({
    searchBookDetails,
    getBookDetail
});
