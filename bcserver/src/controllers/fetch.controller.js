import errorHandler from "@core/handlers/error.handler";
import fetchService from "@services/fetch.service";

async function getBookDetailFromAssetId(req, res) {
    const assetId = req.body.asset_id;

    const bookDetail = await fetchService.getBookDetail(assetId);
    if (!bookDetail) {
        res.status(404).send({
            message: "Cannot find any book detail with this asset id"
        });
    }
    res.send(bookDetail);
}

async function getEmail(req, res) {
    const publicKey = req.body.public_key;
    const email = await fetchService.getEmail(publicKey);
    if (!email) {
        res.status(400).send({
            message: "Cannot find any email with this public key"
        });
    }
    res.send({
        email
    });
}

export default errorHandler({
    getBookDetailFromAssetId,
    getEmail
});
