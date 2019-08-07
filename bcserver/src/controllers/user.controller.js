import errorHandler from "@core/handlers/error.handler";
import userLogic from "@logics/user.logic";
import bookLogic from "@logics/book.logic";

async function getProfile(req, res) {
    const user = await userLogic.getProfile(req.email);

    const profile = {
        email: user.email,
        type: user.type,
        fullname: user.fullname,
        phone: user.phone
    };

    res.send(profile);
    return;
}

async function updateProfile(req, res) {
    const body = req.body;

    const updatedUser = await userLogic.updateProfile(
        req.email,
        body.fullname,
        body.phone
    );

    res.send(updatedUser);
    return;
}

async function getCurrentBook(req, res) {
    const books = await userLogic.getCurrentBook(req.publicKey);

    res.send(books);
}

async function getReturningBook(req, res) {
    const books = await userLogic.getInQueueBook(req.email, true);

    res.send(books);
}

async function getRequestingBook(req, res) {
    const books = await userLogic.getInQueueBook(req.email, false);

    res.send(books);
}

async function getKeepingAmount(req, res) {
    const keeping = await userLogic.getCurrentBook(req.publicKey);
    const returning = await userLogic.getInQueueBook(req.email, true);
    const requesting = await userLogic.getInQueueBook(req.email, false);

    res.send({
        keeping: keeping.length,
        returning: returning.length,
        requesting: requesting.length
    });
}

async function getTransferHistory(req, res) {
    const books = await userLogic.getTransferHistory(req.publicKey);

    res.send(books);
}

async function getLastTransactionTime(req, res) {
    const body = req.body;
    const time = await userLogic.getLastTransactionTime(body.asset_id);

    res.send({ time });
}

async function getBookInformationOfAssetId(req, res) {
    const data = await bookLogic.getBookDetailIdOfAssetId(req.body.asset_id);

    res.send({
        book_detail: data.bookDetail,
        email: data.email
    });
}

export default errorHandler({
    getProfile,
    updateProfile,
    getCurrentBook,
    getReturningBook,
    getRequestingBook,
    getKeepingAmount,
    getTransferHistory,
    getLastTransactionTime,
    getBookInformationOfAssetId
});
