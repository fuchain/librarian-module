import errorHandler from "@core/handlers/error.handler";
import userService from "@services/user.service";

async function getProfile(req, res) {
    const user = await userService.getProfile(req.email);

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

    const updatedUser = await userService.updateProfile(
        req.email,
        body.fullname,
        body.phone
    );

    res.send(updatedUser);
    return;
}

async function getCurrentBook(req, res) {
    const books = await userService.getCurrentBook(req.publicKey);

    res.send(books);
}

async function getReturningBook(req, res) {
    const books = await userService.getInQueueBook(req.email, true);

    res.send(books);
}

async function getRequestingBook(req, res) {
    const books = await userService.getInQueueBook(req.email, false);

    res.send(books);
}

async function getKeepingAmount(req, res) {
    const keeping = await userService.getCurrentBook(req.publicKey);
    const returning = await userService.getInQueueBook(req.email, true);
    const requesting = await userService.getInQueueBook(req.email, false);

    res.send({
        keeping: keeping.length,
        returning: returning.length,
        requesting: requesting.length
    });
}

async function getTransferHistory(req, res) {
    const books = await userService.getTransferHistory(req.publicKey);

    res.send(books);
}

async function getLastTransactionTime(req, res) {
    const body = req.body;
    const time = await userService.getLastTransactionTime(body.asset_id);

    res.send({ time });
}

export default errorHandler({
    getProfile,
    updateProfile,
    getCurrentBook,
    getReturningBook,
    getRequestingBook,
    getKeepingAmount,
    getTransferHistory,
    getLastTransactionTime
});
