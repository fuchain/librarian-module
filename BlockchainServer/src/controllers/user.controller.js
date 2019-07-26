import errorHandler from "@core/handlers/error.handler";
import userService from "@services/user.service";
import env from "@core/env";

async function getProfile(req, res) {
    const body = req.body;

    if (body.public_key === env.publicKey) {
        res.send({
            email: "librarian@fptu.tech",
            type: "librarian",
            fullname: "Thủ Thư",
            phone: "0123456789"
        });
    }

    const user = await userService.getProfile(body.public_key);

    if (user) {
        const profile = {
            email: user.email,
            type: user.type,
            fullname: user.fullname,
            phone: user.phone
        };

        if (req.email !== profile.email) {
            res.status(422).send({
                message: "Your token and public key are not match"
            });
        }

        res.send(profile);
        return;
    }

    return res.status(422).send({
        message: "Cannot find profile with your public key"
    });
}

async function updateProfile(req, res) {
    const body = req.body;

    const user = await userService.getProfile(body.public_key);

    if (user) {
        const profile = {
            email: user.email,
            type: user.type,
            fullname: user.fullname,
            phone: user.phone
        };

        if (req.email !== profile.email) {
            res.status(422).send({
                message: "Your token and public key are not match"
            });
        }

        const updatedUser = await userService.updateProfile(
            req.email,
            body.fullname,
            body.phone
        );

        res.send(updatedUser);
        return;
    }

    return res.status(422).send({
        message: "Cannot find profile with your public key"
    });
}

async function getCurrentBook(req, res) {
    const body = req.body;

    const books = await userService.getCurrentBook(body.public_key);

    res.send(books);
}

async function getReturningBook(req, res) {
    const body = req.body;

    const books = await userService.getInQueueBook(body.public_key, true);

    res.send(books);
}

async function getRequestingBook(req, res) {
    const body = req.body;

    const books = await userService.getInQueueBook(body.public_key, false);

    res.send(books);
}

async function getKeepingAmount(req, res) {
    const body = req.body;

    const keeping = await userService.getCurrentBook(body.public_key);
    const returning = await userService.getInQueueBook(body.public_key, true);
    const requesting = await userService.getInQueueBook(body.public_key, false);

    res.send({
        keeping: keeping.length,
        returning: returning.length,
        requesting: requesting.length
    });
}

async function getTransferHistory(req, res) {
    const body = req.body;

    const books = await userService.getTransferHistory(body.public_key);

    res.send(books);
}

export default errorHandler({
    getProfile,
    updateProfile,
    getCurrentBook,
    getReturningBook,
    getRequestingBook,
    getKeepingAmount,
    getTransferHistory
});
