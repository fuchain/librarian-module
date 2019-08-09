import errorHandler from "@core/handlers/error.handler";
import fetchLogic from "@logics/fetch.logic";

async function getBookDetailFromAssetId(req, res) {
    const assetId = req.body.asset_id;

    const bookDetail = await fetchLogic.getBookDetail(assetId);

    bookDetail
        ? res.send(bookDetail)
        : res.status(404).send({
              message: "Cannot find any book detail with this asset id"
          });
}

async function getEmail(req, res) {
    const publicKey = req.body.public_key;
    const email = await fetchLogic.getEmail(publicKey);

    email
        ? res.send({
              email
          })
        : res.status(400).send({
              message: "Cannot find any email with this public key"
          });
}

export default errorHandler({
    getBookDetailFromAssetId,
    getEmail
});
