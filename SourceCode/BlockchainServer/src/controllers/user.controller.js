import userService from "@services/user.service";

async function getProfile(req, res) {
    const body = req.body;

    const found = await userService.getProfile(body.public_key);

    if (found) {
        const profile = {
            email: found[0].asset.data.email,
            type: found[0].asset.data.type
        };

        res.send(profile);
    }

    return res.status(422).send();
}

async function getCurrentBook(req, res) {
    const body = req.body;

    const books = await userService.getCurrentBook(body.public_key);

    res.send(books);
}

async function getKeepingAmount(req, res) {
    const body = req.body;

    const books = await userService.getCurrentBook(body.public_key);

    res.send({
        keeping: books.length,
        returning: 0,
        requesting: 0
    });
}

async function getTransferHistory(req, res) {
    const body = req.body;

    const books = await userService.getTransferHistory(body.public_key);

    res.send(books);
}

export default {
    getProfile,
    getCurrentBook,
    getKeepingAmount,
    getTransferHistory
};
